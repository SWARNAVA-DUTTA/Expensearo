package com.example.expensearo.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.expensearo.Fragments.AprilFragment;
import com.example.expensearo.Fragments.AugustFragment;
import com.example.expensearo.Fragments.DecemberFragment;
import com.example.expensearo.Fragments.FebruaryFragment;
import com.example.expensearo.Fragments.JanuaryFragment;
import com.example.expensearo.Fragments.JulyFragment;
import com.example.expensearo.Fragments.JuneFragment;
import com.example.expensearo.Fragments.MarchFragment;
import com.example.expensearo.Fragments.MayFragment;
import com.example.expensearo.Fragments.NovemberFragment;
import com.example.expensearo.Fragments.OctoberFragment;
import com.example.expensearo.Fragments.SeptemberFragment;


public class TabsAccessorAdapter extends FragmentStatePagerAdapter {
    public TabsAccessorAdapter(FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                JanuaryFragment januaryFragment=new JanuaryFragment();
                return  januaryFragment;
            case 1:
                FebruaryFragment februaryFragment=new FebruaryFragment();
                return  februaryFragment;
            case 2:
                MarchFragment marchFragment=new MarchFragment();
                return  marchFragment;
            case 3:
                AprilFragment aprilFragment=new AprilFragment();
                return  aprilFragment;
            case 4:
                MayFragment mayFragment=new MayFragment();
                return  mayFragment;
            case 5:
                JuneFragment juneFragment=new JuneFragment();
                return  juneFragment;
            case 6:
                JulyFragment julyFragment=new JulyFragment();
                return  julyFragment;
            case 7:
                AugustFragment augustFragment=new AugustFragment();
                return  augustFragment;
            case 8:
                SeptemberFragment septemberFragment=new SeptemberFragment();
                return  septemberFragment;
            case 9:
                OctoberFragment octoberFragment=new OctoberFragment();
                return  octoberFragment;
            case 10:
                NovemberFragment novemberFragment=new NovemberFragment();
                return  novemberFragment;
            case 11:
                DecemberFragment decemberFragment=new DecemberFragment();
                return  decemberFragment;
            default:return null;
        }
    }

    @Override
    public int getCount() {
        return 12;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position)
    {
        switch (position)
        {
            case 0:
                return  "January";
            case 1:
                return  "February";
            case 2:
                return  "March";
            case 3:
                return  "April";
            case 4:
                return  "May";
            case 5:
                return  "June";
            case 6:
                return  "July";
            case 7:
                return  "August";
            case 8:
                return  "September";
            case 9:
                return  "October";
            case 10:
                return  "November";
            case 11:
                return  "December";


            default:return null;
        }
    }
}
