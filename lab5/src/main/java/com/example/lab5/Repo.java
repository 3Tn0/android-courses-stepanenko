package com.example.lab5;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.core.util.ObjectsCompat;

public class Repo implements Parcelable {

	public String fullName;
	public String description;
	public String url;

	public Repo(@NonNull String fullName, String description, @NonNull String url) {
		this.fullName = fullName;
		this.description = description;
		this.url = url;

	}

	protected Repo(Parcel in) {
		fullName = in.readString();
		description = in.readString();
		url = in.readString();
	}

	public static final Creator<Repo> CREATOR = new Creator<Repo>() {
		@Override
		public Repo createFromParcel(Parcel in) {
			return new Repo(in);
		}

		@Override
		public Repo[] newArray(int size) {
			return new Repo[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(fullName);
		dest.writeString(description);
		dest.writeString(url);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Repo)) return false;
		Repo repo = (Repo) o;
		return fullName.equals(repo.fullName) &&
				description.equals(repo.description)&&
				url.equals(repo.url);
	}

	@Override
	public int hashCode() {
		return ObjectsCompat.hash(fullName, description, url);
	}

	@Override
	public String toString() {
		return "Repo{" +
				"fullName='" + fullName + '\'' +
				", description='" + description + '\'' +
				", url='" + url + '\'' +
				'}';
	}
}
