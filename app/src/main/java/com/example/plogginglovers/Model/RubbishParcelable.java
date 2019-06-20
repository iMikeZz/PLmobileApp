package com.example.plogginglovers.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class RubbishParcelable implements Parcelable {
    private Integer id;
    private String name;
    private String image;
    private Integer score;
    private Integer quantity = 0;

    public RubbishParcelable(Rubbish rubbish) {
        this.id = rubbish.getId();
        this.name = rubbish.getName();
        this.image = rubbish.getImage();
        this.score = rubbish.getScore();
    }

    protected RubbishParcelable(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        name = in.readString();
        image = in.readString();
        if (in.readByte() == 0) {
            score = null;
        } else {
            score = in.readInt();
        }
        if (in.readByte() == 0) {
            quantity = null;
        } else {
            quantity = in.readInt();
        }
    }

    public static final Creator<RubbishParcelable> CREATOR = new Creator<RubbishParcelable>() {
        @Override
        public RubbishParcelable createFromParcel(Parcel in) {
            return new RubbishParcelable(in);
        }

        @Override
        public RubbishParcelable[] newArray(int size) {
            return new RubbishParcelable[size];
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(name);
        dest.writeString(image);
        if (score == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(score);
        }
        if (quantity == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(quantity);
        }
    }
}
