package sim;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;

import sim.tetris.Game;

import com.google.api.services.bigquery.model.TableFieldSchema;
import com.google.api.services.bigquery.model.TableRow;
import com.google.api.services.bigquery.model.TableSchema;
/**
 * Sets up a GCP Dataflow pipeline to simulate thousands of playthroughs of Tetris,
 * and saves the results to BigQuery.
 */
public class Simulator {
	
	/** The GCS project name */
	private static final String PROJECT_ID = "gameanalytics-199018";

	/** The dataset name for the BigQuery output table */
	private static final String dataset = "tetris";

	/** The table name for the BigQuery output table */
	private static final String table = "sim_results"	;
	
	/** Provide an interface for setting theGCS temp location */
	interface Options extends PipelineOptions, Serializable {
		String getTempLocation();
	    void setTempLocation(String value);
	}
	

	/**
	 * Run the level simulation pipeline.
	 */
	public static void main(String[] args) {
		
		// create different random seeds to evaluate
		Random rand = new Random();
		ArrayList<Integer> seeds = new ArrayList<>();
		for (int i=0; i<10; i++) {
			seeds.add(rand.nextInt());
		}

	    // create the schema for the results table
	    List<TableFieldSchema> fields = new ArrayList<>();
	    fields.add(new TableFieldSchema().setName("levels").setType("INT64"));
	    fields.add(new TableFieldSchema().setName("heightFactor").setType("FLOAT64"));
	    fields.add(new TableFieldSchema().setName("balanceFactor").setType("FLOAT64"));
	    fields.add(new TableFieldSchema().setName("holeFactor").setType("FLOAT64"));
	    fields.add(new TableFieldSchema().setName("blockFactor").setType("FLOAT64"));
	    fields.add(new TableFieldSchema().setName("lineFactor").setType("FLOAT64"));
	    TableSchema schema = new TableSchema().setFields(fields);
		
		// set up the dataflow pipeline 
	    Simulator.Options options = PipelineOptionsFactory.fromArgs(args).withValidation().as(Simulator.Options.class);
	    Pipeline pipeline = Pipeline.create(options);

	    // create a PCollection using the seeds collection 
	    pipeline.apply(Create.of(seeds)) 
	    
	    // run a game simulation for each of the seeds
        .apply("Simulate Games", ParDo.of(new DoFn<Integer, TableRow>() {

        	@ProcessElement
        	public void processElement(ProcessContext c) throws Exception {
        		Integer seed = c.element();
        	
        		// play the game
	      		Game game = new Game(seed);
	    		int levels = game.runSimulation();
	      			    		
	      		// save the results 
	    		TableRow results = new TableRow();
	    		results.set("levels", levels);
	    		results.set("heightFactor", game.getHeightFactor());
	    		results.set("balanceFactor", game.getBalanceFactor());
	    		results.set("holeFactor", game.getHoleFactor());
	    		results.set("blockFactor", game.getBlockFactor());
	    		results.set("lineFactor", game.getLineFactor());
	    		
	    		// pass the stats to the next step in the pipeline 
	            c.output(results);
            }
        }))
	    // write the results to BigQuery 	
	    .apply(BigQueryIO.writeTableRows() .to(String.format("%s:%s.%s", PROJECT_ID, dataset, table))
	            .withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_IF_NEEDED)
	            .withWriteDisposition(BigQueryIO.Write.WriteDisposition.WRITE_TRUNCATE)
	            .withSchema(schema)
	    );
	    
	    // run the pipeline
	    pipeline.run();
	}
}
