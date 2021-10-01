package com.nc.edu.ta.scherbak;


import java.io.*;
import java.util.Date;
import java.util.Objects;

public class Task implements Serializable {
    private String title;
    private boolean active;
    private Date startTime;
    private Date endTime;
    private int repeatInterval;
    public Task(String title, Date time) {
        this(title, time, time,0);
    }
    public Task(String title, Date startTime, Date endTime, int repeatInterval) {
        if(title == null || startTime == null || endTime == null
                || repeatInterval < 0 || startTime.after(endTime)){
            throw new RuntimeException();
        }
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.repeatInterval = repeatInterval;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if(title == null){
            throw new RuntimeException();
        }
        this.title = title;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Date getTime() {
        return startTime;
    }

    public void setTime(Date time) {
        setTime(time, time, 0);
    }

    public void setTime(Date start, Date end, int repeat) {
        if(start == null || end == null
                || repeat < 0 || start.after(end)){
            throw new RuntimeException();
        }
        this.startTime = start;
        this.endTime = end;
        this.repeatInterval = repeat;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public int getRepeatInterval() {
        return repeatInterval;
    }

    public boolean isRepeated() {
        return repeatInterval != 0;
    }

    @Override
    public String toString() {
        return getStringByTitle("", title);
    }

    public String getStringByTitle(String titlePrefix, String title) {
        StringBuilder builder = new StringBuilder();
        builder.append("Task ").append("\"").append(titlePrefix).append(title).append("\"");
        if (isActive()) {
            if (isRepeated()) {
                builder.append(" from ").append(startTime).append(" to ").append(endTime);
                builder.append(" every ").append(repeatInterval).append(" seconds");
            } else {
                builder.append(" at ").append(startTime);
            }
        } else {
            builder.append(" is inactive");
        }
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return active == task.active
                && Objects.equals(startTime,task.startTime)
                && Objects.equals(endTime,task.endTime)
                && repeatInterval == task.repeatInterval
                && Objects.equals(title, task.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, active, startTime, endTime, repeatInterval);
    }

    public Date nextTimeAfter(Date time) {
        if(time == null){
            throw new RuntimeException();
        }
        if (isActive()) {
            if (isRepeated()) {
                for (Date tmp = getStartTime(); tmp.compareTo(getEndTime()) <= 0; tmp = new Date(tmp.getTime() + getRepeatInterval() * 1000L)) {
                    if(time.before(tmp)) {
                        return tmp;
                    }
                }
            } else {
                if(time.before(getTime())) {
                    return getTime();
                }
            }
        }
        return null;
    }

    @Override
    public Task clone() throws CloneNotSupportedException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream ous = new ObjectOutputStream(baos);
            ous.writeObject(this);
            ous.close();
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (Task) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new CloneNotSupportedException();
        }
    }
}
