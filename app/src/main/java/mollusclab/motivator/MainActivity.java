package mollusclab.motivator;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends Activity {
    private GraphicalView mChart;
    private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
    private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
    private XYSeries mCurrentSeries;
    private XYSeriesRenderer mCurrentRenderer;
    StatisticDB db = null;
    private int record_day;
    private int aim_score_month = 340;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new StatisticDB(this);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.incrementTodayScore(1);
                refresh();
            }
        });
        button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.incrementTodayScore(2);
                refresh();
            }
        });
        button = (Button) findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.incrementTodayScore(3);
                refresh();
            }
        });
        button = (Button) findViewById(R.id.button4);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.incrementTodayScore(4);
                refresh();
            }
        });
        button = (Button) findViewById(R.id.button5);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.incrementTodayScore(5);
                refresh();
            }
        });
        button = (Button) findViewById(R.id.button6);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.incrementTodayScore(6);
                refresh();
            }
        });
        button = (Button) findViewById(R.id.button7);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.incrementTodayScore(7);
                refresh();
            }
        });
        button = (Button) findViewById(R.id.button8);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.incrementTodayScore(8);
                refresh();
            }
        });
        button = (Button) findViewById(R.id.button9);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.incrementTodayScore(9);
                refresh();
            }
        });
        button = (Button) findViewById(R.id.button10);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.incrementTodayScore(10);
                refresh();
            }
        });
        refresh();
    }

    private void refresh(){
        refreshChart();
        refreshStatistic();
        refreshTodayScore();
    }

    private void refreshStatistic() {
        TextView tw = (TextView) findViewById(R.id.record_month);
        int record_month = db.getRecordMonthScore();
        tw.setText(String.valueOf(record_month));

        tw = (TextView) findViewById(R.id.record_day);
        record_day = db.getRecordDayScore();
        tw.setText(String.valueOf(record_day));

        tw = (TextView) findViewById(R.id.score_month);
        int score_month = db.getMonthScore();
        tw.setText(String.valueOf(score_month));
        if(record_month < score_month) tw.setTextColor(Color.GREEN);
        int delta = record_month - score_month;
        int colorText = 0;
        if(delta < 0) {
            colorText = Color.GREEN;
            tw.setTypeface(null, Typeface.BOLD);
        }
        else{
            //colorText = Color.HSVToColor( new float[]{120-delta*120/record_day,1,1});
            colorText = Color.rgb(((delta*255)/record_day), 255 - (delta*255)/record_day ,0 );
        }
        tw.setTextColor(colorText);

        tw = (TextView) findViewById(R.id.aim_score_month);
        tw.setText(String.valueOf(aim_score_month));

        tw = (TextView) findViewById(R.id.aim_progress_month);
        tw.setText(String.valueOf(score_month/aim_score_month) + "%");
    }

    private void refreshTodayScore(){
        TextView tw = (TextView) findViewById(R.id.current_value);
        int score = db.getTodayScore();
        tw.setText(String.valueOf(score));
        int colorText = 0;
        int delta = record_day - score;
        if(delta < 0) {
            colorText = Color.GREEN;
            tw.setTypeface(null, Typeface.BOLD);
        }
        else{
            colorText = Color.rgb(((delta*255)/record_day), 255 - (delta*255)/record_day ,0 );
        }
        tw.setTextColor(colorText);
    }

    private void refreshChart(){
        LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
        initChart();
        addData();
        mChart = ChartFactory.getLineChartView(this, mDataset, mRenderer);
        layout.removeAllViews();
        layout.addView(mChart);
    }

    private void initChart() {
        mCurrentSeries = new XYSeries("Sample Data");
        mDataset = new XYMultipleSeriesDataset();
        mDataset.addSeries(mCurrentSeries);
        mCurrentRenderer = new XYSeriesRenderer();
        mCurrentRenderer.setLineWidth(2);
        mCurrentRenderer.setColor(Color.RED);
        mCurrentRenderer.setShowLegendItem(false);
// Include low and max value
        mCurrentRenderer.setDisplayBoundingPoints(true);
// we add point markers
        mCurrentRenderer.setPointStyle(PointStyle.CIRCLE);
        //mCurrentRenderer.setPointStrokeWidth(3);
        mRenderer = new XYMultipleSeriesRenderer();
        mRenderer.addSeriesRenderer(mCurrentRenderer);
        mRenderer.setXLabels(0);
// We want to avoid black border
        mRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00)); // transparent margins
        mRenderer.setLabelsTextSize(25);
        mRenderer.setXLabelsColor(Color.BLUE);
        mRenderer.setYLabelsPadding(20);
        mRenderer.setYLabelsColor(0, Color.BLUE);

// Disable Pan on two axis
        mRenderer.setPanEnabled(true, false);
        mRenderer.setShowGrid(true); // we show the grid
        mRenderer.setYAxisMin(0);
    }

    private void addData() {
        HashMap<Long, Integer> list = db.getListData();

        for ( Long k : list.keySet()) {
            mCurrentSeries.add(k, list.get(k));
        }
        Calendar c = Calendar.getInstance();
        Date to = c.getTime();
        c.add(Calendar.DATE, -7);
        Date from = c.getTime();
        mRenderer.setXAxisMax(to.getTime());
        mRenderer.setXAxisMin(from.getTime());
        DateFormat df = new SimpleDateFormat("dd.MM");
        for ( Long k : list.keySet()) {
            Date d = new Date(k);
            mRenderer.addXTextLabel(k,df.format(d));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}