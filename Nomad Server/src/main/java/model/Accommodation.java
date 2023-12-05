package model;

import jakarta.persistence.*;
import model.enums.AccommodationStatus;
import model.enums.ConfirmationType;

import java.util.ArrayList;
import java.util.List;


public class Accommodation {

    private long id;
    private AppUser host;
    private int minGuests;
    private int maxGuests;
    private String name;
    private String description;
    private String address;
    private List<String> amenities;
    private List<String> images;
    private List<Comment> comments;
    private AccommodationStatus status;
    private ConfirmationType confirmationType;
    private int deadlineForCancellation;
    private boolean verified;
    public Accommodation(){}

    // Constructor
    public Accommodation(AppUser host, int minGuests, int maxGuests, String name, String description, String address, List<String> amenities,
                         List<String> images, AccommodationStatus status, ConfirmationType confirmationType, int deadlineForCancellation) {
        this.host = host;
        this.minGuests = minGuests;
        this.maxGuests = maxGuests;
        this.name = name;
        this.description = description;
        this.address = address;
        this.amenities = amenities;
        this.images = images;
        this.comments = new ArrayList<Comment>();
        this.status = status;
        this.confirmationType = confirmationType;
        this.deadlineForCancellation = deadlineForCancellation;
        this.verified = false;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public ConfirmationType getConfirmationType() {
        return confirmationType;
    }

    public void setConfirmationType(ConfirmationType confirmationType) {
        this.confirmationType = confirmationType;
    }

    public int getDeadlineForCancellation() {
        return deadlineForCancellation;
    }

    public void setDeadlineForCancellation(int deadlineForCancellation) {
        this.deadlineForCancellation = deadlineForCancellation;
    }
    public AccommodationStatus getStatus() {
        return status;
    }

    public void setStatus(AccommodationStatus status) {
        this.status = status;
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


    public AppUser getHost() {
        return host;
    }

    public void setHost(AppUser host) {
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
                "id=" + id +
                ", host=" + host +
                ", minGuests=" + minGuests +
                ", maxGuests=" + maxGuests +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", address='" + address + '\'' +
                ", amenities=" + amenities +
                ", images=" + images +
                ", comments=" + comments +
                ", status=" + status +
                ", confirmationType=" + confirmationType +
                ", deadlineForCancellation=" + deadlineForCancellation +
                '}';
    }

    public void copyValues(Accommodation accommodation) {
        this.minGuests = accommodation.minGuests;
        this.maxGuests = accommodation.maxGuests;
        this.name = accommodation.name;
        this.description = accommodation.description;
        this.address = accommodation.address;
        this.amenities = accommodation.amenities;
        this.images = accommodation.images;
        this.status = accommodation.status;
        this.deadlineForCancellation = accommodation.deadlineForCancellation;
        this.confirmationType = accommodation.confirmationType;
    }
}