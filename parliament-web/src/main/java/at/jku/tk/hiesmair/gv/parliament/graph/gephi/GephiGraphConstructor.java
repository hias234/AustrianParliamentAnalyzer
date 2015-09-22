package at.jku.tk.hiesmair.gv.parliament.graph.gephi;


import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.util.concurrent.TimeUnit;

import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.io.database.drivers.PostgreSQLDriver;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.io.exporter.preview.PNGExporter;
import org.gephi.io.exporter.preview.SVGExporter;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.importer.plugin.database.EdgeListDatabaseImpl;
import org.gephi.io.importer.plugin.database.ImporterEdgeList;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.layout.plugin.AutoLayout;
import org.gephi.layout.plugin.force.StepDisplacement;
import org.gephi.layout.plugin.force.yifanHu.YifanHuLayout;
import org.gephi.layout.plugin.forceAtlas.ForceAtlasLayout;
import org.gephi.layout.spi.LayoutProperty;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.gephi.visualization.VizController;
import org.openide.util.Lookup;

import com.mysql.jdbc.ExportControlled;

public class GephiGraphConstructor {

	public String doWork(){
		ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
		pc.newProject();
		Workspace workspace = pc.getCurrentWorkspace();
		
		ImportController importController = Lookup.getDefault().lookup(ImportController.class);
		EdgeListDatabaseImpl db = new EdgeListDatabaseImpl();
		db.setDBName("parliament");
		db.setHost("localhost");
		db.setUsername("parliament_user");
		db.setPasswd("parliament");
		db.setSQLDriver(new PostgreSQLDriver());
		db.setPort(5432);
		
		db.setNodeQuery("select politician.id as id, sur_name as label from politician " +
						"inner join mandate on (mandate_type = 'NationalCouncilMember' and mandate.politician_id = politician.id) " +
						"inner join ncm_period on (ncm_id = mandate.id) where period = 25");
		
//		db.setNodeAttributesQuery("");
		
		db.setEdgeQuery("select politician1_id as source, politician2_id as target, sum(weight) as weight from politician_attitude_relation " +
						"inner join discussion on (discussion.id = discussion_id) " +
						"inner join session on (session.id = session_id) " + 
						"where period = 25 " +
						"group by politician1_id, politician2_id");
		
		Container container = importController.importDatabase(db, new ImporterEdgeList());
		importController.process(container, new DefaultProcessor(), workspace);
		
		GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getModel();
		
		AutoLayout autoLayout = new AutoLayout(30, TimeUnit.SECONDS);
		autoLayout.setGraphModel(graphModel);
		YifanHuLayout firstLayout = new YifanHuLayout(null, new StepDisplacement(1f));
		ForceAtlasLayout secondLayout = new ForceAtlasLayout(null);
		
		AutoLayout.DynamicProperty adjustBySizeProperty = AutoLayout.createDynamicProperty("forceAtlas.adjustSizes.name", true, 0.1F);
		AutoLayout.DynamicProperty repulsionProperty = AutoLayout.createDynamicProperty("forceAtlas.repulsionStrength.name", new Double(500.), 0f);
		
		autoLayout.addLayout(firstLayout, 0.5f);
		autoLayout.addLayout(secondLayout, 0.5f, new AutoLayout.DynamicProperty[] { adjustBySizeProperty, repulsionProperty });
		
		autoLayout.execute();
		
		VizController.getInstance().getTextManager().getModel().setShowNodeLabels(true);
		
		ExportController ec = Lookup.getDefault().lookup(ExportController.class);
		SVGExporter svgExporter = (SVGExporter) ec.getExporter("svg");
		
		StringWriter writer = new StringWriter();
		ec.exportWriter(writer, svgExporter);
		
		return writer.toString();
	}
	
}
