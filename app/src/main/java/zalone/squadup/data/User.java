package zalone.squadup.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.TimingLogger;

import com.google.firebase.firestore.Exclude;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class User implements Parcelable {

    private String name;
    private String photoUrl;
    private String epicName;
    private Date lastAsked;
    private Date lastSquaddingUp;

    // Bio Fields

    private String favDropSpot;
    private String favGamemode;
    private String snapchat;
    private String instagram;

    private List<String> following;

    private double lon;
    private double lat;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public User() {}

    public User(String name, String photoUrl, String epicName, Date lastAsked, Date lastSquaddingUp, String favDropSpot, String favGamemode, String snapchat, String instagram, List<String> following, double lon, double lat) {
        this.name = name;
        this.photoUrl = photoUrl;
        this.epicName = epicName;
        this.lastAsked = lastAsked;
        this.lastSquaddingUp = lastSquaddingUp;
        this.favDropSpot = favDropSpot;
        this.favGamemode = favGamemode;
        this.snapchat = snapchat;
        this.instagram = instagram;
        this.following = following;
        this.lon = lon;
        this.lat = lat;
    }

    private static final int SECONDS_IN_MINUTE = 60;
    private static final int SECONDS_IN_HOUR = 60 * SECONDS_IN_MINUTE;
    private static final int SECONDS_IN_DAY = 24 * SECONDS_IN_HOUR;
    private static final int SECONDS_IN_WEEK = 7 * SECONDS_IN_DAY;

    @Exclude
    public String getFormattedLastSquaddingUp() {
        Date now = new Date();

        long seconds = (now.getTime() - this.lastSquaddingUp.getTime()) / 1000;

        if (seconds < SECONDS_IN_MINUTE) {
            return seconds + " seconds";
        } else if (seconds < SECONDS_IN_HOUR) {
            int minutes = (int) (seconds / SECONDS_IN_MINUTE);
            return minutes + " minutes";
        } else  if (seconds < SECONDS_IN_DAY){
            int hours = (int) (seconds / SECONDS_IN_HOUR);
            return hours + " hours";
        } else if (seconds < 2 * SECONDS_IN_WEEK) {
            int days = (int) (seconds / SECONDS_IN_DAY);
            return days + " days";
        } else {
            int weeks = (int) (seconds / SECONDS_IN_WEEK);
            return weeks + " weeks";
        }
    }

    @Exclude
    private String firebaseId = "";

    @Exclude
    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }

    @Exclude
    public String getFirebaseId() {
        return firebaseId;
    }

    @Exclude
    public boolean isCompleteProfile() {
        return this.name != null && this.epicName != null;
    }

    @Exclude
    public boolean shouldAskWantToSquadUp() {
        if (this.lastAsked == null) {
            return true;
        }

        Date fourtyMinutesAgo = new Date(System.currentTimeMillis() - (40 * 60 * 1000));
        return this.lastAsked.before(fourtyMinutesAgo);
    }

    @Exclude
    public void setAskedResult(boolean wantToSquadUp) {
        this.lastAsked = new Date();

        if (wantToSquadUp) {
            this.lastSquaddingUp = new Date();
        }
    }

    public String getName() {
        return name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getEpicName() {
        return epicName;
    }

    public Date getLastAsked() {
        return lastAsked;
    }

    public Date getLastSquaddingUp() {
        return lastSquaddingUp;
    }

    public String getFavDropSpot() {
        return favDropSpot;
    }

    public String getFavGamemode() {
        return favGamemode;
    }

    public String getSnapchat() {
        return snapchat;
    }

    public String getInstagram() {
        return instagram;
    }

    public List<String> getFollowing() {
        return following;
    }

    public void setFollowing(List <String> following) {
        this.following = following;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", epicName='" + epicName + '\'' +
                ", lastAsked=" + lastAsked +
                ", lastSquaddingUp=" + lastSquaddingUp +
                ", favDropSpot='" + favDropSpot + '\'' +
                ", favGamemode='" + favGamemode + '\'' +
                ", snapchat='" + snapchat + '\'' +
                ", instagram='" + instagram + '\'' +
                ", following=" + following +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Double.compare(user.getLon(), getLon()) == 0 &&
                Double.compare(user.getLat(), getLat()) == 0 &&
                Objects.equals(getName(), user.getName()) &&
                Objects.equals(getPhotoUrl(), user.getPhotoUrl()) &&
                Objects.equals(getEpicName(), user.getEpicName()) &&
                Objects.equals(getLastAsked(), user.getLastAsked()) &&
                Objects.equals(getLastSquaddingUp(), user.getLastSquaddingUp()) &&
                Objects.equals(getFavDropSpot(), user.getFavDropSpot()) &&
                Objects.equals(getFavGamemode(), user.getFavGamemode()) &&
                Objects.equals(getSnapchat(), user.getSnapchat()) &&
                Objects.equals(getInstagram(), user.getInstagram()) &&
                Objects.equals(getFollowing(), user.getFollowing()) &&
                Objects.equals(getFirebaseId(), user.getFirebaseId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getPhotoUrl(), getEpicName(), getLastAsked(), getLastSquaddingUp(), getFavDropSpot(), getFavGamemode(), getSnapchat(), getInstagram(), getFollowing(), getLon(), getLat(), getFirebaseId());
    }

    // Parcelable methods

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.photoUrl);
        dest.writeString(this.epicName);
        dest.writeLong(this.lastAsked != null ? this.lastAsked.getTime() : -1);
        dest.writeLong(this.lastSquaddingUp != null ? this.lastSquaddingUp.getTime() : -1);
        dest.writeString(this.favDropSpot);
        dest.writeString(this.favGamemode);
        dest.writeString(this.snapchat);
        dest.writeString(this.instagram);
        dest.writeStringList(this.following);
        dest.writeDouble(this.lon);
        dest.writeDouble(this.lat);
        dest.writeString(this.firebaseId);
    }

    protected User(Parcel in) {
        this.name = in.readString();
        this.photoUrl = in.readString();
        this.epicName = in.readString();
        long tmpLastAsked = in.readLong();
        this.lastAsked = tmpLastAsked == -1 ? null : new Date(tmpLastAsked);
        long tmpLastSquaddingUp = in.readLong();
        this.lastSquaddingUp = tmpLastSquaddingUp == -1 ? null : new Date(tmpLastSquaddingUp);
        this.favDropSpot = in.readString();
        this.favGamemode = in.readString();
        this.snapchat = in.readString();
        this.instagram = in.readString();
        this.following = in.createStringArrayList();
        this.lon = in.readDouble();
        this.lat = in.readDouble();
        this.firebaseId = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
