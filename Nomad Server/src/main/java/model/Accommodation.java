package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Accommodation {
    private long id;
    private User host;
    private int minGuests;
    private int maxGuests;
    private String name;
    private String description;
    private String address;
    private List<String> amenities;
    private List<String> images;
    private List<Comment> comments;

    // Constructor
    public Accommodation(User host, int minGuests, int maxGuests, String name, String description, String address, List<String> amenities, List<String> images) {
        this.host = host;
        this.minGuests = minGuests;
        this.maxGuests = maxGuests;
        this.name = name;
        this.description = description;
        this.address = address;
        this.amenities = amenities;
        this.images = images;
        this.comments = new ArrayList<Comment>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void addComment(Comment comment){
        this.comments.add(comment);
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }


    public void addImage(String image){
        this.images.add(image);
    }


    public User getHost() {
        return host;
    }

    public void setHost(User host) {
        this.host = host;
    }

    public int getMinGuests() {
        return minGuests;
    }

    public void setMinGuests(int minGuests) {
        this.minGuests = minGuests;
    }

    public int getMaxGuests() {
        return maxGuests;
    }

    public void setMaxGuests(int maxGuests) {
        this.maxGuests = maxGuests;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<String> amenities) {
        this.amenities = amenities;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "Accommodation{" +
                "host=" + host +
                ", minGuests=" + minGuests +
                ", maxGuests=" + maxGuests +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", address='" + address + '\'' +
                ", amenities=" + amenities +
                ", images=" + images +
                '}';
    }


}