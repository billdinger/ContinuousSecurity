import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class FakeLog {

    public String renderJson() {
      return "";
    };
}
