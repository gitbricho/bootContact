package bootviewer.model;

import java.util.Date;
import java.util.Map;

public class Snippet {
  private Date publishedAt;
  private String channelId;
  private String title;
  private String description;
  private Map<String, Thumbnail> thumbnails;
  private String channelTitle;

  public Date getPublishedAt() {
    return publishedAt;
  }

  public void setPublishedAt(Date publishedAt) {
    this.publishedAt = publishedAt;
  }

  public String getChannelId() {
    return channelId;
  }

  public void setChannelId(String channelId) {
    this.channelId = channelId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Map<String, Thumbnail> getThumbnails() {
    return thumbnails;
  }

  public void setThumbnails(Map<String, Thumbnail> thumbnails) {
    this.thumbnails = thumbnails;
  }

  public String getChannelTitle() {
    return channelTitle;
  }

  public void setChannelTitle(String channelTitle) {
    this.channelTitle = channelTitle;
  }

}
