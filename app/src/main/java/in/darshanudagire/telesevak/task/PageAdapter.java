package in.darshanudagire.telesevak.task;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import in.darshanudagire.telesevak.task.TabLayout.Completed;
import in.darshanudagire.telesevak.task.TabLayout.Pending;

public class PageAdapter extends FragmentStatePagerAdapter {

    int noOfTabs;
    String pname,pdesc,phone;
    public PageAdapter(@NonNull FragmentManager fm, int behavior,String pname,String phone) {
        super(fm, behavior);
        noOfTabs = behavior;
        this.pname = pname;
        this.phone = phone;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                Pending pending = new Pending(pname,phone);
                return pending;
            case 1:
                Completed completed = new Completed(pname,phone);
                return completed;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return noOfTabs;
    }
}
