package com.example.plogginglovers.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class ActivityParcelable implements Parcelable {
    private Integer id;
    private String name;
    private String startTime;
    private String endTime;
    private String location;
    private String description;
    private String type;
    private String duration;
    private String state;
    private String teamStatus;
    private Integer teamId;
    private String responsibleTeacher;


    protected ActivityParcelable(Parcel in) {
        id = in.readInt();
        name = in.readString();
        startTime = in.readString();
        endTime = in.readString();
        location = in.readString();
        description = in.readString();
        type = in.readString();
        duration = in.readString();
        state = in.readString();
        teamStatus = in.readString();
        teamId = in.readInt();
        responsibleTeacher = in.readString();
    }

    public ActivityParcelable(Activity activity) {
        this.id = activity.getId();
        this.name = activity.getName();
        this.startTime = activity.getStartTime();
        this.endTime = activity.getEndTime();
        this.location = activity.getLocation();
        this.description = activity.getDescription();
        this.duration = activity.getDuration() == null ? "N/A" : String.valueOf(activity.getDuration());
        this.type = activity.getType();
        this.state = activity.getState();
        this.teamStatus = activity.getTeamStatus();
        this.teamId = activity.getTeamId();
        this.responsibleTeacher = activity.getResponsibleTeacher();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeString(location);
        dest.writeString(description);
        dest.writeString(type);
        dest.writeString(duration);
        dest.writeString(state);
        dest.writeString(teamStatus);
        dest.writeInt(teamId);
        dest.writeString(responsibleTeacher);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ActivityParcelable> CREATOR = new Creator<ActivityParcelable>() {
        @Override
        public ActivityParcelable createFromParcel(Parcel in) {
            return new ActivityParcelable(in);
        }

        @Override
        public ActivityParcelable[] newArray(int size) {
            return new ActivityParcelable[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTeamStatus() {
        return teamStatus;
    }

    public void setTeamStatus(String teamStatus) {
        this.teamStatus = teamStatus;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public String getResponsibleTeacher() {
        return responsibleTeacher;
    }

    public void setResponsibleTeacher(String responsibleTeacher) {
        this.responsibleTeacher = responsibleTeacher;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
