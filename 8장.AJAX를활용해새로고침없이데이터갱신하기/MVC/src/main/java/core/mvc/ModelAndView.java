package core.mvc;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ModelAndView {
    private View view;
    private Map<String, Object> model = new HashMap<>();

    public ModelAndView(View view) {
        this.view = view;
    }

    public ModelAndView addObject(String attributeName, Object attributeValue) {
        model.put(attributeName, attributeValue);
        return this;
    }

    public Map<String, Object> getModel() {
        // 해당 Map에 대한 수정이 불가능하다.
        // 객체의 불변성을 유지하면서 외부에 맵 데이터를 공개할 때 데이터의 무결성을 보호하는 것
        return Collections.unmodifiableMap(model);
    }

    public View getView() {
        return view;
    }
}
