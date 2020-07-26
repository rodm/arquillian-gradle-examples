package arquillian.validation;

import static arquillian.validation.Code.Type.B;

public class CodeBean {

    public void setCodeTypeA(@Code String code) {
        System.out.println("Setting code for type A");
    }

    public void setCodeTypeB(@Code(type = B) String code) {
        System.out.println("Setting code for type B");
    }
}
