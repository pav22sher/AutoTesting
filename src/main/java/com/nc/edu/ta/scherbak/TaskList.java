package com.nc.edu.ta.scherbak;

import java.io.*;

public abstract class TaskList extends AbstractTaskList{
    @Override
    public TaskList clone(){
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream ous = new ObjectOutputStream(baos);
            ous.writeObject(this);
            ous.close();
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (TaskList) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }
    @Override
    public int hashCode() {
        int hashCode = 1;
        for (Task task : this) {
            hashCode = 31 * hashCode + (task == null ? 0 : task.hashCode());
        }
        return hashCode;
    }
}
