package game.weekend.seedskeeper.dictionaries.other;

import game.weekend.seedskeeper.data.types.Error;
import game.weekend.seedskeeper.data.types.Record;
import game.weekend.seedskeeper.general.Loc;

public class QualityData extends Record {
    private int id;
    private int numb;
    private String code;
    private String color;

    public QualityData() {
        this(0, 1, "", "");
    }

    public QualityData(int id, int numb, String code, String color) {
        setId(id);
        setNumb(numb);
        setCode(code);
        setColor(color);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumb() {
        return numb;
    }

    public void setNumb(int numb) {
        this.numb = numb;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        if (color == null || color.trim().length() != 6)
            color = "ffffff";
        this.color = color.trim();
    }

    public Error check() {
        if (getCode() == null || getCode().trim().length() == 0)
            return new Error(Loc.get("enter_the_quality_code") + ".", 2);

        return null;
    }

    @Override
    public boolean hasDifference(Object o) {
        StringBuilder sb = new StringBuilder();
        setDifferences("");

        if (this == o)
            return false;

        QualityData other = (QualityData) o;

        boolean result;
        result = checkDifference(Loc.get("code_is"), code, other.code, sb);
        result |= checkDifference(Loc.get("color_is"), color, other.color, sb);

        if (result)
            setDifferences(sb.toString());

        return result;
    }
}
