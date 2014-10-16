package addressgenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Vector;
import queue.Q;
import queue.QManager;

public class AddressGenerator {

    private static Vector<Address> addresses = new Vector<Address>();
    private static ArrayList<Object> objs = new ArrayList<Object>();

    public static void main(String[] args) throws IllegalArgumentException {

        String outDir = "";
        int genCap = 0;
        int poolsize = 0;
        int numGen = 0;
        String[] ps = null;

        if (args.length < 4) {
            String documentation
                    = "\n\nList of Arguments (Syntax: flag=arg) "
                    + "All Arguments are Required \n"
                    + "\n"
                    + "--poolsize   -p\n"
                    + "--numofgen   -n\n"
                    + "--gencap     -g\n"
                    + "--output     -o\n"
                    + "\n"
                    + "Number of Addresses Generated is equal to: numofgen*gencap"
                    + "\nTo make full use of poolsize, numofgen>=poolsize.\n\n";
            System.out.print(documentation);
            System.exit(0);
        } else if (args.length == 4) {
            for (String arg : args) {
                if ((arg.startsWith("--poolsize=")) || (arg.startsWith("-p="))) {
                    ps = arg.split("=");
                    if (ps.length == 2) {
                        try {
                            poolsize = Integer.parseInt(ps[1]);
                        } catch (Exception e) {
                            throw new IllegalArgumentException("Invalid " + arg + " argument");
                        }
                    } else {
                        throw new IllegalArgumentException("Invalid " + arg + " argument");
                    }
                }
                if ((arg.startsWith("--numofgen=")) || (arg.startsWith("-n="))) {
                    ps = arg.split("=");
                    if (ps.length== 2) {
                        try {
                            numGen = Integer.parseInt(ps[1]);
                        } catch (Exception e) {
                            throw new IllegalArgumentException("Invalid " + arg + " argument");
                        }
                    } else {
                        throw new IllegalArgumentException("Invalid " + arg + " argument");
                    }
                }
                if ((arg.startsWith("--genCap=")) || (arg.startsWith("-g="))) {
                    ps = arg.split("=");
                    if (ps.length == 2) {
                        try {
                            genCap = Integer.parseInt(ps[1]);
                        } catch (Exception e) {
                            throw new IllegalArgumentException("Invalid " + arg + " argument");
                        }
                    } else {
                        throw new IllegalArgumentException("Invalid " + arg + " argument");
                    }
                }
                if ((arg.startsWith("--output=")) || (arg.startsWith("-o="))) {
                    ps = arg.split("=");
                    if (ps.length == 2) {
                        try {
                            outDir = ps[1];
                        } catch (Exception e) {
                            throw new IllegalArgumentException("Invalid " + arg + " argument");
                        }
                    } else {
                        throw new IllegalArgumentException("Invalid " + arg + " argument");
                    }
                }
            }
        }
        System.out.println("Generating " + (numGen * genCap) + " Valid Addresses");
        System.out.println("With a PoolSize of " + poolsize + " Generating Concurrently");

        QManager manager = new QManager();

        for (int i = 0; i < numGen; i++) {
            objs.add((Object) new Generator(genCap, addresses));
        }

        Q q = manager.getPool(poolsize, objs);
        long start = System.currentTimeMillis();
        q.start(false);
        q.waitFor();
        long end = System.currentTimeMillis();
        writeOut(outDir);

        System.out.println("Generated " + addresses.size());
        System.out.println("Failed: " + ((numGen * genCap)-addresses.size()));
        DecimalFormat df = new DecimalFormat("#.000");
        System.out.println("Average Time (s) Taken Per Generated Address: " + df.format(new Double((end-start)/1000)/new Double(addresses.size())));

    }

    public static void writeOut(String path) {
        Writer writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(new File(path), true));
        } catch (IOException ex) {
        }
        for (Address addr : addresses) {
            try {
                CSVReader.writeLine(writer, addr.csvOut());
            } catch (Exception ex) {
            }
        }
        try {
            writer.flush();
            writer.close();
        } catch (IOException ex) {
        }
    }

}
