package com.arif.reportpanel;

import java.text.ParseException;

import org.achartengine.GraphicalView;
import org.achartengine.model.TimeSeries;

import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public abstract class AbstractPanel {
	abstract public long getFormId();
	abstract public void setFormId(long formId);
	abstract protected TimeSeries getSeries(long formId, long fieldId) throws ParseException;
	abstract protected GraphicalView getChart(long formId, long fieldId);
	abstract protected void setNumerikNDateField();
	abstract protected OnClickListener actionOptions();
	abstract protected void showOptionDialog();
	abstract public void setContainer(LinearLayout container);
	abstract public LinearLayout getContainer();
	abstract public void created();
}
