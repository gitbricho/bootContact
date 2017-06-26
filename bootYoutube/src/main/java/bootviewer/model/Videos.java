package bootviewer.model;

import java.util.ArrayList;
import java.util.List;

public class Videos {
  private String kind;
  private String etag;
  private String nextPageToken;
  private String regionCode;
  private PageInfo pageInfo;
  private List<Item> items = new ArrayList<>();

  public Videos() {
  }

  public String getKind() {
    return kind;
  }

  public void setKind(String kind) {
    this.kind = kind;
  }

  public String getEtag() {
    return etag;
  }

  public void setEtag(String etag) {
    this.etag = etag;
  }

  public String getNextPageToken() {
    return nextPageToken;
  }

  public void setNextPageToken(String nextPageToken) {
    this.nextPageToken = nextPageToken;
  }

  public String getRegionCode() {
    return regionCode;
  }

  public void setRegionCode(String regionCode) {
    this.regionCode = regionCode;
  }

  public PageInfo getPageInfo() {
    return pageInfo;
  }

  public void setPageInfo(PageInfo pageInfo) {
    this.pageInfo = pageInfo;
  }

  public List<Item> getItems() {
    return items;
  }

  public void setItems(List<Item> items) {
    this.items = items;
  }

  @Override
  public String toString() {
    return "Videos [kind=" + kind + ", etag=" + etag + ", nextPageToken=" + nextPageToken
        + ", regionCode=" + regionCode + ", pageInfo=" + pageInfo + "]";
  }
}
