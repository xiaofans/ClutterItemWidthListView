package xiaofan.clutterwidthitemlistview;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import xiaofan.clutterwidthitemlistview.clutterlistview.ClutterListView;


public class MyActivity extends ActionBarActivity {

    private ClutterListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        setUpViews();
    }

    private void setUpViews() {
        listView = (ClutterListView) findViewById(R.id.clutter_list);
        String[] data = new String[]{"一","一二","一二三","一二三四","一二三四五","一二三四五六","一二三四五六七","一二三四五六八","一二三四五六九",
        "天干物燥","再唱不出那样的歌曲","因为爱情,不会轻易悲伤","我们好像在哪见过,你记得吗"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
