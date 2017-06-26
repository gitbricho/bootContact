package bootviewer.model;

public class Item {
  private String kind;
  private String etag;
  private VId id;
  private Snippet snippet;

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

  public VId getId() {
    return id;
  }

  public void setId(VId id) {
    this.id = id;
  }

  public Snippet getSnippet() {
    return snippet;
  }

  public void setSnippet(Snippet snippet) {
    this.snippet = snippet;
  }

  @Override
  public String toString() {
    return "Item [kind=" + kind + ", etag=" + etag + ", id=" + id + "]";
  }
}
