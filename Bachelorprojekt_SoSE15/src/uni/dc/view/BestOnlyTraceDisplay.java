package uni.dc.view;

import java.awt.Dimension;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import uni.dc.ubsOpti.tracer.BestOnlyTracer;
import uni.dc.ubsOpti.tracer.TracerStat;

public class BestOnlyTraceDisplay extends JFrame {
	private static final long serialVersionUID = 1L;

	public BestOnlyTraceDisplay(String title, BestOnlyTracer traces) {
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
		localXYLineAndShapeRenderer.setBaseToolTipGenerator(new XYToolTipGenerator() {
			@Override
			public String generateToolTip(XYDataset dataset, int series, int item) {
				String res = String.format("%s: (%d/%.4e)", dataset.getSeriesKey(series),
						dataset.getX(series, item).intValue(), dataset.getYValue(series, item));
				return res;
			}
		});
		NumberAxis localNumberAxis = (NumberAxis) localXYPlot.getRangeAxis();
		localNumberAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		return localJFreeChart;
	}

	public XYSeriesCollection readDataset(BestOnlyTracer tracer) {
		XYSeriesCollection series = new XYSeriesCollection();

		Map<String, List<TracerStat>> stats = tracer.getStats();

		for (String algoName : stats.keySet()) {
			XYSeries serie = new XYSeries(algoName);
			for (TracerStat stat : stats.get(algoName)) {
				serie.add(stat.getStep(), stat.getDelay());
			}
			series.addSeries(serie);
		}
		return series;
	}
}
