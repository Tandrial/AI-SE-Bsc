package uni.dc.view;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import uni.dc.ubsOpti.tracer.DelayTrace;
import uni.dc.ubsOpti.tracer.TracerStat;

public class TraceDisplay extends JFrame {
	private static final long serialVersionUID = 1L;

	public TraceDisplay(String title, List<DelayTrace> traces) {
		super(title);

		JFreeChart localJFreeChart = createChart(readDataset(traces));
		ChartPanel localChartPanel = new ChartPanel(localJFreeChart);
		localChartPanel.setMouseWheelEnabled(true);

		localChartPanel.setPreferredSize(new Dimension(500, 300));
		setContentPane(localChartPanel);
	}

	private static JFreeChart createChart(XYDataset paramXYDataset) {
		JFreeChart localJFreeChart = ChartFactory.createXYLineChart("TraceDelay", "Step", "Fitness", paramXYDataset,
				PlotOrientation.VERTICAL, true, true, false);

		XYPlot localXYPlot = (XYPlot) localJFreeChart.getPlot();
		localXYPlot.setDomainAxis(new LogarithmicAxis("Step"));
		localXYPlot.setDomainPannable(true);
		localXYPlot.setRangePannable(true);

		XYLineAndShapeRenderer localXYLineAndShapeRenderer = (XYLineAndShapeRenderer) localXYPlot.getRenderer();
		localXYLineAndShapeRenderer.setBaseShapesVisible(true);
		localXYLineAndShapeRenderer.setBaseShapesFilled(true);
		NumberAxis localNumberAxis = (NumberAxis) localXYPlot.getRangeAxis();
		localNumberAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		return localJFreeChart;
	}

	public XYSeriesCollection readDataset(List<DelayTrace> traces) {
		XYSeriesCollection series = new XYSeriesCollection();
		for (DelayTrace trace : traces) {
			XYSeries serie = new XYSeries(trace.getName());
			for (TracerStat stat : trace.getStats()) {
				serie.add(stat.getStep(), stat.getDelay());
			}
			series.addSeries(serie);
		}
		return series;
	}
}
