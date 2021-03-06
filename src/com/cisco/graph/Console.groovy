package com.cisco.graph


import com.tinkerpop.gremlin.Gremlin;
import com.tinkerpop.gremlin.Imports;
import jline.History;
import org.codehaus.groovy.tools.shell.Groovysh;
import org.codehaus.groovy.tools.shell.IO;
import org.codehaus.groovy.tools.shell.InteractiveShellRunner;
import com.tinkerpop.gremlin.console.ResultHookClosure
import com.tinkerpop.gremlin.console.PromptClosure
import com.tinkerpop.gremlin.console.ErrorHookClosure
import com.tinkerpop.gremlin.console.NullResultHookClosure;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class Console {

    private static final String HISTORY_FILE = ".gremlin_history";
    private static final String STANDARD_INPUT_PROMPT = "gremlin> ";
    private static final String STANDARD_RESULT_PROMPT = "==>";


    public Console(final IO io, final String inputPrompt, final String resultPrompt) {
        io.out.println();
        io.out.println("         \\,,,/");
        io.out.println("         (o o)");
        io.out.println("-----oOOo-(_)-oOOo-----");

        final Groovysh groovy = new Groovysh();
        groovy.setResultHook(new NullResultHookClosure(groovy));
        for (String imps : Imports.getImports()) {
            groovy.execute("import " + imps);
        }
        groovy.setResultHook(new ResultHookClosure(groovy, io, resultPrompt));
        groovy.setHistory(new History());

        final InteractiveShellRunner runner = new InteractiveShellRunner(groovy, new PromptClosure(groovy, inputPrompt));
        runner.setErrorHandler(new ErrorHookClosure(runner, io));
        try {
            runner.setHistory(new History(new File(HISTORY_FILE)));
        } catch (IOException e) {
            io.err.println("Unable to create history file: " + HISTORY_FILE);
        }

        Gremlin.load();
        try {
            runner.run();
        } catch (Error e) {
            //System.err.println(e.getMessage());
        }
    }

    public Console() {
        this(new IO(System.in, System.out, System.err), STANDARD_INPUT_PROMPT, STANDARD_RESULT_PROMPT);
    }


    public static void main(final String[] args) {
        new Console();
    }
}
