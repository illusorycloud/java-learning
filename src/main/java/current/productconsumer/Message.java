package current.productconsumer;

import java.util.Date;

/**
 * 生产者消费者模式
 * 生产的对象
 * Message
 *
 * @author illusoryCloud
 */
public class Message {
    private String Title;
    private String Content;
    private String Tail;
    private Date Time;

    public Message() {
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public String getTitle() {
        return Title;
    }

    public String getContent() {
        return Content;
    }

    public String getTail() {
        return Tail;
    }

    public Date getTime() {
        return Time;
    }

    /**
     * 静态内部类
     */
    public static class Builder {
        private String Title;
        private String Content;
        private String Tail;
        private Date Time;

        public Builder setTitle(String title) {
            this.Title = title;
            return this;
        }

        public Builder setContent(String content) {
            this.Content = content;
            return this;
        }

        public Builder setTail(String tail) {
            this.Tail = tail;
            return this;
        }

        public Builder setTime(Date time) {
            this.Time = time;
            return this;
        }

        public Message Build() {
            Message message = new Message();
            message.Title = Title;
            message.Content = Content;
            message.Tail = Tail;
            message.Time = Time;
            return message;
        }
    }
}
