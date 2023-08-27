package dev.wuason.mechanics.data;

import dev.wuason.mechanics.utils.Utils;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

public class Data implements Serializable {
    final private Date creationDate = new Date();
    private Date lastDataAccesDate;
    private Date lastDataSaveDate;
    private String id;
    private String dataType;
    private String data;

    public Data(String id) {
        this.id = id;
    }
    public void setDataObject(Object data){
        this.dataType = data.getClass().getSimpleName();
        try {
            this.data = Utils.serializeObjectBukkit(data);
            lastDataSaveDate = new Date();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void setData(String data,String dataType){
        this.dataType = dataType;
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getLastDataAccesDate() {
        return lastDataAccesDate;
    }

    public Date getLastDataSaveDate() {
        return lastDataSaveDate;
    }

    public String getDataType() {
        return dataType;
    }

    public String getData() {
        return data;
    }

    public Object getDataObject(){
        try {
            lastDataAccesDate = new Date();
            return Utils.deserializeObjectBukkit(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
