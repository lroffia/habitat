package it.unibo.disi.ai.app;

//import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;

//import org.jbpm.process.builder.ProcessBuildContext;

public class DemoLauncher {
	private static String javaCmd="/Library/Java/JavaVirtualMachines/jdk1.8.0_91.jdk/Contents/Home/bin/java";
	private static String javaOpt="-Dfile.encoding=UTF-8";
	
	private static String javaClasspath="/Users/utente/Dropbox/DOTTORATO/ws-mars/Habitat/target/classes:"
			+ "/Users/utente/Dropbox/DOTTORATO/ws-mars/Habitat/lib/jdom-2.0.6.jar:"
			+ "/Users/utente/Dropbox/DOTTORATO/ws-mars/Habitat/lib/KPI.jar:"
			+ "/Users/utente/Dropbox/DOTTORATO/ws-mars/Habitat/lib/SEPATools.jar:"
			+ "/Users/utente/Dropbox/DOTTORATO/ws-mars/Habitat/miglayout15-swing.jar:"
			+ "/Users/utente/.m2/repository/org/kie/kie-api/6.4.0.Final/kie-api-6.4.0.Final.jar:"
			+ "/Users/utente/.m2/repository/org/drools/drools-core/6.4.0.Final/drools-core-6.4.0.Final.jar:"
			+ "/Users/utente/.m2/repository/org/mvel/mvel2/2.2.8.Final/mvel2-2.2.8.Final.jar:"
			+ "/Users/utente/.m2/repository/org/kie/kie-internal/6.4.0.Final/kie-internal-6.4.0.Final.jar:"
			+ "/Users/utente/.m2/repository/commons-codec/commons-codec/1.4/commons-codec-1.4.jar:"
			+ "/Users/utente/.m2/repository/org/drools/drools-decisiontables/6.4.0.Final/drools-decisiontables-6.4.0.Final.jar:"
			+ "/Users/utente/.m2/repository/org/drools/drools-compiler/6.4.0.Final/drools-compiler-6.4.0.Final.jar:"
			+ "/Users/utente/.m2/repository/org/antlr/antlr-runtime/3.5/antlr-runtime-3.5.jar:"
			+ "/Users/utente/.m2/repository/org/eclipse/jdt/core/compiler/ecj/4.4.2/ecj-4.4.2.jar:"
			+ "/Users/utente/.m2/repository/com/thoughtworks/xstream/xstream/1.4.7/xstream-1.4.7.jar:"
			+ "/Users/utente/.m2/repository/xmlpull/xmlpull/1.1.3.1/xmlpull-1.1.3.1.jar:"
			+ "/Users/utente/.m2/repository/xpp3/xpp3_min/1.1.4c/xpp3_min-1.1.4c.jar:"
			+ "/Users/utente/.m2/repository/com/google/protobuf/protobuf-java/2.6.0/protobuf-java-2.6.0.jar:"
			+ "/Users/utente/.m2/repository/org/drools/drools-templates/6.4.0.Final/drools-templates-6.4.0.Final.jar:"
			+ "/Users/utente/.m2/repository/org/apache/poi/poi-ooxml/3.13/poi-ooxml-3.13.jar:"
			+ "/Users/utente/.m2/repository/org/apache/poi/poi-ooxml-schemas/3.13/poi-ooxml-schemas-3.13.jar:"
			+ "/Users/utente/.m2/repository/org/apache/xmlbeans/xmlbeans/2.6.0/xmlbeans-2.6.0.jar:"
			+ "/Users/utente/.m2/repository/stax/stax-api/1.0.1/stax-api-1.0.1.jar:/Users/utente/.m2/repository/org/apache/poi/poi/3.13/poi-3.13.jar:"
			+ "/Users/utente/.m2/repository/org/jbpm/jbpm-test/6.4.0.Final/jbpm-test-6.4.0.Final.jar:"
			+ "/Users/utente/.m2/repository/org/jbpm/jbpm-bpmn2/6.4.0.Final/jbpm-bpmn2-6.4.0.Final.jar:"
			+ "/Users/utente/.m2/repository/org/jbpm/jbpm-flow-builder/6.4.0.Final/jbpm-flow-builder-6.4.0.Final.jar"
			+ ":/Users/utente/.m2/repository/org/jbpm/jbpm-flow/6.4.0.Final/jbpm-flow-6.4.0.Final.jar:"
			+ "/Users/utente/.m2/repository/joda-time/joda-time/1.6.2/joda-time-1.6.2.jar:"
			+ "/Users/utente/.m2/repository/org/jbpm/jbpm-persistence-jpa/6.4.0.Final/jbpm-persistence-jpa-6.4.0.Final.jar:"
			+ "/Users/utente/.m2/repository/org/drools/drools-persistence-jpa/6.4.0.Final/drools-persistence-jpa-6.4.0.Final.jar:"
			+ "/Users/utente/.m2/repository/org/jbpm/jbpm-audit/6.4.0.Final/jbpm-audit-6.4.0.Final.jar:"
			+ "/Users/utente/.m2/repository/org/jbpm/jbpm-query-jpa/6.4.0.Final/jbpm-query-jpa-6.4.0.Final.jar:"
			+ "/Users/utente/.m2/repository/org/codehaus/jackson/jackson-mapper-asl/1.9.9/jackson-mapper-asl-1.9.9.jar:"
			+ "/Users/utente/.m2/repository/org/jbpm/jbpm-human-task-core/6.4.0.Final/jbpm-human-task-core-6.4.0.Final.jar:"
			+ "/Users/utente/.m2/repository/javax/activation/activation/1.1.1/activation-1.1.1.jar:"
			+ "/Users/utente/.m2/repository/org/jboss/spec/javax/annotation/jboss-annotations-api_1.1_spec/1.0.1.Final/jboss-annotations-api_1.1_spec-1.0.1.Final.jar:"
			+ "/Users/utente/.m2/repository/javax/mail/mail/1.4.5/mail-1.4.5.jar:/Users/utente/.m2/repository/org/codehaus/jackson/jackson-core-asl/1.9.9/jackson-core-asl-1.9.9.jar:/Users/utente/.m2/repository/org/jbpm/jbpm-human-task-workitems/6.4.0.Final/jbpm-human-task-workitems-6.4.0.Final.jar:/Users/utente/.m2/repository/org/jbpm/jbpm-shared-services/6.4.0.Final/jbpm-shared-services-6.4.0.Final.jar:/Users/utente/.m2/repository/org/jbpm/jbpm-workitems/6.4.0.Final/jbpm-workitems-6.4.0.Final.jar:/Users/utente/.m2/repository/commons-io/commons-io/2.1/commons-io-2.1.jar:/Users/utente/.m2/repository/org/apache/commons/commons-compress/1.4.1/commons-compress-1.4.1.jar:/Users/utente/.m2/repository/org/tukaani/xz/1.0/xz-1.0.jar:/Users/utente/.m2/repository/org/apache/commons/commons-exec/1.3/commons-exec-1.3.jar:/Users/utente/.m2/repository/org/jdom/jdom/1.1.3/jdom-1.1.3.jar:/Users/utente/.m2/repository/org/apache/cxf/cxf-api/2.7.18/cxf-api-2.7.18.jar:/Users/utente/.m2/repository/org/codehaus/woodstox/woodstox-core-asl/4.4.1/woodstox-core-asl-4.4.1.jar:/Users/utente/.m2/repository/org/codehaus/woodstox/stax2-api/3.1.4/stax2-api-3.1.4.jar:/Users/utente/.m2/repository/org/apache/cxf/cxf-rt-frontend-jaxws/2.7.18/cxf-rt-frontend-jaxws-2.7.18.jar:/Users/utente/.m2/repository/xml-resolver/xml-resolver/1.2/xml-resolver-1.2.jar:/Users/utente/.m2/repository/asm/asm/3.3.1/asm-3.3.1.jar:/Users/utente/.m2/repository/org/apache/cxf/cxf-rt-core/2.7.18/cxf-rt-core-2.7.18.jar:/Users/utente/.m2/repository/org/apache/cxf/cxf-rt-bindings-soap/2.7.18/cxf-rt-bindings-soap-2.7.18.jar:/Users/utente/.m2/repository/org/apache/cxf/cxf-rt-bindings-xml/2.7.18/cxf-rt-bindings-xml-2.7.18.jar:/Users/utente/.m2/repository/org/apache/cxf/cxf-rt-frontend-simple/2.7.18/cxf-rt-frontend-simple-2.7.18.jar:/Users/utente/.m2/repository/org/apache/cxf/cxf-rt-ws-addr/2.7.18/cxf-rt-ws-addr-2.7.18.jar:/Users/utente/.m2/repository/org/apache/cxf/cxf-rt-ws-policy/2.7.18/cxf-rt-ws-policy-2.7.18.jar:/Users/utente/.m2/repository/org/apache/cxf/cxf-rt-databinding-jaxb/2.7.18/cxf-rt-databinding-jaxb-2.7.18.jar:/Users/utente/.m2/repository/com/sun/xml/bind/jaxb-impl/2.1.13/jaxb-impl-2.1.13.jar:/Users/utente/.m2/repository/com/sun/xml/bind/jaxb-xjc/2.1.13/jaxb-xjc-2.1.13.jar:/Users/utente/.m2/repository/com/sun/xml/bind/jaxb-core/2.2.11/jaxb-core-2.2.11.jar:/Users/utente/.m2/repository/org/apache/cxf/cxf-rt-transports-http/2.7.18/cxf-rt-transports-http-2.7.18.jar:/Users/utente/.m2/repository/wsdl4j/wsdl4j/1.6.3/wsdl4j-1.6.3.jar:/Users/utente/.m2/repository/org/apache/neethi/neethi/3.0.2/neethi-3.0.2.jar:/Users/utente/.m2/repository/org/apache/ws/xmlschema/xmlschema-core/2.0.2/xmlschema-core-2.0.2.jar:/Users/utente/.m2/repository/org/apache/httpcomponents/httpcore/4.3.3/httpcore-4.3.3.jar:/Users/utente/.m2/repository/org/apache/httpcomponents/httpclient/4.3.6/httpclient-4.3.6.jar:/Users/utente/.m2/repository/commons-logging/commons-logging/1.1.3/commons-logging-1.1.3.jar:/Users/utente/.m2/repository/com/fasterxml/jackson/core/jackson-databind/2.4.0/jackson-databind-2.4.0.jar:/Users/utente/.m2/repository/com/fasterxml/jackson/core/jackson-annotations/2.4.0/jackson-annotations-2.4.0.jar:/Users/utente/.m2/repository/com/fasterxml/jackson/core/jackson-core/2.4.0/jackson-core-2.4.0.jar:/Users/utente/.m2/repository/org/jbpm/jbpm-runtime-manager/6.4.0.Final/jbpm-runtime-manager-6.4.0.Final.jar:/Users/utente/.m2/repository/org/eclipse/aether/aether-api/1.0.0.v20140518/aether-api-1.0.0.v20140518.jar:/Users/utente/.m2/repository/org/jbpm/jbpm-human-task-audit/6.4.0.Final/jbpm-human-task-audit-6.4.0.Final.jar:/Users/utente/.m2/repository/org/jbpm/jbpm-human-task-jpa/6.4.0.Final/jbpm-human-task-jpa-6.4.0.Final.jar:/Users/utente/.m2/repository/com/h2database/h2/1.3.168/h2-1.3.168.jar:/Users/utente/.m2/repository/org/hibernate/hibernate-entitymanager/4.2.21.Final/hibernate-entitymanager-4.2.21.Final.jar:/Users/utente/.m2/repository/org/jboss/logging/jboss-logging/3.1.0.GA/jboss-logging-3.1.0.GA.jar:/Users/utente/.m2/repository/dom4j/dom4j/1.6.1/dom4j-1.6.1.jar:/Users/utente/.m2/repository/org/hibernate/common/hibernate-commons-annotations/4.0.2.Final/hibernate-commons-annotations-4.0.2.Final.jar:/Users/utente/.m2/repository/org/hibernate/hibernate-core/4.2.21.Final/hibernate-core-4.2.21.Final.jar:/Users/utente/.m2/repository/antlr/antlr/2.7.7/antlr-2.7.7.jar:/Users/utente/.m2/repository/org/hibernate/javax/persistence/hibernate-jpa-2.0-api/1.0.1.Final/hibernate-jpa-2.0-api-1.0.1.Final.jar:/Users/utente/.m2/repository/org/codehaus/btm/btm/2.1.4/btm-2.1.4.jar:/Users/utente/.m2/repository/org/javassist/javassist/3.18.1-GA/javassist-3.18.1-GA.jar:/Users/utente/.m2/repository/org/jboss/spec/javax/transaction/jboss-transaction-api_1.1_spec/1.0.1.Final/jboss-transaction-api_1.1_spec-1.0.1.Final.jar:/Users/utente/.m2/repository/junit/junit/4.11/junit-4.11.jar:/Users/utente/.m2/repository/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar:/Users/utente/.m2/repository/org/slf4j/slf4j-api/1.7.21/slf4j-api-1.7.21.jar:/Users/utente/.m2/repository/org/slf4j/slf4j-simple/1.7.21/slf4j-simple-1.7.21.jar";
	
	public static void main(String[] args)  {
		try{
			/*
			File f= new File("/Users/utente/Dropbox/DOTTORATO/workspace/habitat/OSGI_SIB_v4/");
			ProcessBuilder sibPb = new ProcessBuilder("java", "-jar", 
					"/Users/utente/Dropbox/DOTTORATO/workspace/habitat/OSGI_SIB_v4/org.eclipse.osgi_3.11.0.v20160603-1336.jar");
			sibPb.redirectOutput(Redirect.INHERIT);
			sibPb.redirectError(Redirect.INHERIT);
			sibPb.directory(f.getAbsoluteFile());
			Process sib = sibPb.start();
			*/
			
			//Thread mmupSimulator = new Thread(new MapMultiUserPositionSimulator());
			ProcessBuilder mmupPb = new ProcessBuilder(javaCmd, javaOpt, "-classpath",javaClasspath,
					"it.unibo.disi.ai.app.sim.MapMultiUserPositionSimulator");
			mmupPb.redirectOutput(Redirect.INHERIT);
			mmupPb.redirectError(Redirect.INHERIT);
			Process mmup = mmupPb.start();
			//Thread mpMonitor = new Thread(new MapPositionMonitor());
			ProcessBuilder mpPb = new ProcessBuilder(javaCmd, javaOpt, "-classpath",javaClasspath,
					"it.unibo.disi.ai.app.mon.MapPositionMonitor");
			mpPb.redirectOutput(Redirect.INHERIT);
			mpPb.redirectError(Redirect.INHERIT);
			Process mp = mpPb.start();
			
			//Thread opApp = new Thread(new OperatorApp("Alice"));
			ProcessBuilder opAlicePb = new ProcessBuilder(javaCmd, javaOpt, "-classpath",javaClasspath,
					"it.unibo.disi.ai.app.opapp.OperatorApp", "Alice");
			opAlicePb.redirectOutput(Redirect.INHERIT);
			opAlicePb.redirectError(Redirect.INHERIT);
			Process opAlice = opAlicePb.start();			
			
			ProcessBuilder opLucaPb = new ProcessBuilder(javaCmd, javaOpt, "-classpath",javaClasspath,
					"it.unibo.disi.ai.app.opapp.OperatorApp", "Luca");
			opLucaPb.redirectOutput(Redirect.INHERIT);
			opLucaPb.redirectError(Redirect.INHERIT);
			Process opLuca = opLucaPb.start();			
			
			
			System.out.println(Thread.currentThread().getName()+": Press any key to start Ai module...");
			System.in.read();
			//Thread aimApp = new Thread(new AIModuleApplication());
			ProcessBuilder aimApp = new ProcessBuilder(javaCmd, javaOpt, "-classpath",javaClasspath,
					"it.unibo.disi.ai.app.aimodule.AIModuleApplication");
			aimApp.redirectOutput(Redirect.INHERIT);
			aimApp.redirectError(Redirect.INHERIT);
			Process aim = aimApp.start();
			 		
			System.out.println(Thread.currentThread().getName()+": Press any key to stop the simulation...");
			System.in.read();
			
			mmup.destroy();
			mp.destroy();
			opAlice.destroy();
			opLuca.destroy();
			aim.destroy();
			
			//sib.destroy();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
