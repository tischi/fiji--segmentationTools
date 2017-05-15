package segmentationTools;


import automic.parameters.ParameterCollection;
import automic.parameters.ParameterType;
import ij.IJ;
import ij.ImagePlus;
import ij.plugin.PlugIn;

import javax.swing.*;

/**
 * Created by tischi on 24/03/17.
 */
public class AnalyzeFISHSpotsPlugIn implements PlugIn {

    ImagePlus imp;
    AnalyzeFISHSpotsGUI analyzeFISHSpotsGUI;
    ParameterCollection parameterCollection;

    public AnalyzeFISHSpotsPlugIn() {
    }

    public AnalyzeFISHSpotsPlugIn(String path) {
        this();
        IJ.open(path);
    }

    public void run(String arg) {
        this.imp = IJ.getImage();
        initialize();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                showDialog();
            }
        });
    }

    public void showDialog(){
        analyzeFISHSpotsGUI = new AnalyzeFISHSpotsGUI(parameterCollection);
        analyzeFISHSpotsGUI.showDialog();
    }

    private void initialize()
    {
        parameterCollection = getParameterCollection();
    }

    public ParameterCollection getParameterCollection()
    {

        parameterCollection = new ParameterCollection();

        for (int i = 0; i < imp.getNChannels(); i++)
        {
            parameterCollection.addParameter(getChannelFlag(i), null, true, ParameterType.BOOL_PARAMETER);
            parameterCollection.addParameter(getSpotRadiiKey(i), null, "1.5,1.5,3", ParameterType.STRING_PARAMETER);
            parameterCollection.addParameter(getSpotThresholdKey(i), null, 200.0, ParameterType.DOUBLE_PARAMETER);
        }

        parameterCollection.setUndefinedValuesFromDefaults();

        /*
        for (String key : parameterCollection.getParametersIndetifiers())
        {
            Utils.threadLog(key);
            Utils.threadLog(""+parameterCollection.getParameterValue(key));
        }*/

        return parameterCollection;

    }

    public static String getChannelFlag(int iChannel)
    {
        String key = "Analyze Channel " + (iChannel + 1);
        return key;
    }

    public static String getSpotThresholdKey(int iChannel)
    {
        String key = "Spot Threshold Channel " + (iChannel + 1);
        return key;
    }

    public static String getSpotRadiiKey(int iChannel)
    {
        String key = "Spot Radii Channel " + (iChannel + 1);
        return key;
    }

    public static void main(String[] args) {
        // set the plugins.dir property to make the plugin appear in the Plugins menu
        Class<?> clazz = AnalyzeFISHSpotsPlugIn.class;
        String url = clazz.getResource("/" + clazz.getName().replace('.', '/') + ".class").toString();
        String pluginsDir = url.substring("file:".length(), url.length() - clazz.getName().length() - ".class".length());
        System.setProperty("plugins.dir", pluginsDir);

        // start ImageJ
        new ij.ImageJ();
        IJ.wait(1000);

        AnalyzeFISHSpotsPlugIn afs = new AnalyzeFISHSpotsPlugIn("/Users/tischi/Documents/fiji-plugin-segmentationTools/data/DNAFISH_T1_Z50_C4.tif");
        afs.run("");
    }

}
