
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLine;

import flexprettyprint.handlers.ASPrettyPrinter;
import flexprettyprint.handlers.WrapOptions;

public class App {

    private static Options options = new Options();
    // 大括号类型，Sun or Adobe
    private static String _braceStyle = "Sun";
    private static String _indent = "Tabs";

    static {
        // 参数：
        // -input a.as
        // -output b.as
        options.addOption("braceStyle", true, "output filepath");
        options.addOption("indent", true, "indentation character");
        options.addOption("input", true, "input filepath");
        options.addOption("output", true, "output filepath");
    }

    public static void main(String[] args) {
        // for (String var : args) {
        // System.out.println(var);
        // }
        DefaultParser parser = new DefaultParser();
        try {
            CommandLine cLine = parser.parse(options, args);
            if (cLine.hasOption("braceStyle")) {
                _braceStyle = cLine.getOptionValue("braceStyle");
            }
            if (cLine.hasOption("useTabs")) {
                _indent = cLine.getOptionValue("indent");
            }
            String input = cLine.getOptionValue("input");
            String output = cLine.getOptionValue("output");
            System.out.println("braceStyle: " + _braceStyle);
            System.out.println("indent: " + _indent);
            System.out.println(input);
            System.out.println(output);
            if (input != null && output != null && !input.isEmpty() && !output.isEmpty()) {
                handle(input, output);
            }
        } catch (Exception e) {
            System.out.println("error");
            e.printStackTrace();
        }
    }

    private static void handle(String input, String output) {
        File inFile = new File(input);
        try {
            ASPrettyPrinter printer = new ASPrettyPrinter(inFile, "UTF-8");
            printer.setBlockIndent(4);
            // printer.setUseBraceStyleSetting(false);

            // 大括号{}是否是在同行
            if (_braceStyle.equals("Sun")) {
                printer.setBraceStyleSetting(ASPrettyPrinter.BraceStyle_Sun);
            } else {
                printer.setBraceStyleSetting(ASPrettyPrinter.BraceStyle_Adobe);
            }


            printer.setUseTabs(_indent.equals("Tabs"));

            printer.setArrayInitWrapOptions(new WrapOptions(WrapOptions.WRAP_BY_COLUMN_ONLY_ADD_CRS));
            printer.setSpacesInsideParensEtc(0);
            printer.setBlankLinesBeforeControlStatement(0);
            // printer.setEmptyStatementsOnNewLine(false);
            // printer.setKeepBlankLines(true);
            // printer.setBlankLinesToEndFunctions(0);
            // printer.setWrapObjectItemsAlignStart(true);
            // printer.setWrapFirstObjectItem(true);
            // printer.setWrapFirstArrayItem(false);
            // printer.setWrapArrayItemsAlignStart(true);
            String result = printer.print(0);

            // write to file
            writeFile(result, output);
        } catch (Exception e) {
            System.out.println("error");
            e.printStackTrace();
        }
    }

    /**
     * write file
     */
    private static void writeFile(String data, String output) {
        try {
            File outFile = new File(output);
            if (!outFile.exists()) {
                outFile.createNewFile();
            }
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8"));
            writer.write(data);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            System.out.println("error");
            e.printStackTrace();
        }
    }
}
