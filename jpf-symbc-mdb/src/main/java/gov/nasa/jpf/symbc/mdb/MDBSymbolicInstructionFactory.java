package gov.nasa.jpf.symbc.mdb;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.symbc.SymbolicInstructionFactory;
import gov.nasa.jpf.symbc.mdb.bytecode.collectionarray.GETFIELD;
import gov.nasa.jpf.symbc.mdb.schema.SchemaReader;
import gov.nasa.jpf.vm.Instruction;

public class MDBSymbolicInstructionFactory extends SymbolicInstructionFactory {	
	
	public MDBSymbolicInstructionFactory(Config conf) {
		super(conf);
		String[] configPath = conf.getStringArray("symbolic.mdb.schema");
		String resourcePath; 
		if (configPath != null && !configPath[0].equals("")) {
			resourcePath = configPath[0];
		} else {
			resourcePath = "src/examples/resources";
		}
		SchemaReader.invoke(resourcePath);
		
		GETFIELD.useSchemaVersionInitializationHeuristic = conf.getBoolean("symbolic.mdb.initHeuristic", false);
	}
	
	@Override
	public Instruction getfield(String fieldName, String clsName, String fieldDescriptor) {
		return new GETFIELD(fieldName, clsName, fieldDescriptor, super.getfield(fieldName, clsName, fieldDescriptor));
	}
}
