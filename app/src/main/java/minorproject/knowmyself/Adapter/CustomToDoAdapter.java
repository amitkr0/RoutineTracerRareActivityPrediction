package minorproject.knowmyself.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import minorproject.knowmyself.Database.ToDoDBHelper;
import minorproject.knowmyself.Fragment.ToDo;
import minorproject.knowmyself.Other.ToDoBean;
import minorproject.knowmyself.R;

/**
 * Created by Dell on 10-04-2018.
 */

public class CustomToDoAdapter extends ArrayAdapter<ToDoBean> {

    private Context context;
    private int resource;
    private List<ToDoBean> list;

    public CustomToDoAdapter(@NonNull Context context, int resource, @NonNull List<ToDoBean> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.list=objects;
    }

    public View getView(int position, final View convertView, final ViewGroup parent){
        View listItemView = convertView;
        if(listItemView == null){
            LayoutInflater layoutInflater= LayoutInflater.from(context);
            listItemView = layoutInflater.inflate(resource,parent,false);
        }

        TextView task_Title = listItemView.findViewById(R.id.task_title);
        TextView task_details = listItemView.findViewById(R.id.task_details);

        ImageButton deleteButton = listItemView.findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View parentRow = (View) v.getParent();
                ListView listView = (ListView) parentRow.getParent();
                int pos = listView.getPositionForView(parentRow);
                Log.v("abcd","a "+pos+"   ");
                ToDoBean toDoBean = (ToDoBean) listView.getItemAtPosition(pos);
                String event = toDoBean.getEvent();
                new ToDoDBHelper(context).deleteToDo(event);
                list.remove(pos);
                CustomToDoAdapter.this.notifyDataSetChanged();
            }
        });


        ToDoBean todoBean = getItem(position);
        task_Title.setText(todoBean.getEvent());
        task_details.setText(""+todoBean.getLocation()+" "+todoBean.getInTime()+" - "+todoBean.getOutTime());

        return listItemView;
    }
}
