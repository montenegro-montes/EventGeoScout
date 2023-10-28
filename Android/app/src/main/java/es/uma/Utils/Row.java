package es.uma.Utils;

/**
 * Created by monte on 23/11/15.
 */
public class Row {
    private String mlayer;
    private int mtype;
    private int mnelementos;

    private boolean mchecked;

    public String getLayer()
    {
        return mlayer;
    }

    public void setlayer(String layer)
    {
        mlayer = layer;
    }

    public int getType()
    {
        return mtype;
    }

    public void setType(int type)
    {
        mtype = type;
    }

    public boolean isChecked()
    {
        return mchecked;
    }

    public void setChecked(boolean checked)
    {
        mchecked = checked;
    }

    public int getNelements()
    {
        return mnelementos;
    }

    public void setNelements(int nelements)
    {
        mnelementos = nelements;
    }
}