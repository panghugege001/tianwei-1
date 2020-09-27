package dfh.model;

import java.math.BigInteger;

/**
 * Created by Zii on 2017/9/21.
 */
public class GuildCountEach {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    private String name;
    private int count;
    private BigInteger foo;

    public BigInteger getFoo() {
        return foo;
    }

    public void setFoo(BigInteger foo) {
        this.foo = foo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
