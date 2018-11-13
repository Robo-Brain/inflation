package com.robo.Entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_settings")
public class UserSettings {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Getter
    @Setter
    private Integer id;

    @Getter
    @Setter
    @Column(name = "user_id")
    private String userId;

    @Getter
    @Setter
    @Column(name = "shop_id")
    private Integer shopId;

    public UserSettings(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "UserSettings{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", shopId=" + shopId +
                '}';
    }
}
