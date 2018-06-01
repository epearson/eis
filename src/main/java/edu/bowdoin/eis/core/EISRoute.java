package edu.bowdoin.eis.core;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"routeName", "seq"})
})
public class EISRoute {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String routeName;
    @Column(nullable = false)
    private Date createDate;
    @Column(nullable = false)
    private Integer seq;
    @Lob
    @Column(nullable = false)
    private String routeDefinition;
    @Column(nullable = false)
    private String createdBy;
    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Boolean active;

    public EISRoute() {}

    public EISRoute(String routeName,
                    String routeDefinition) {
        this.routeName = routeName;
        this.routeDefinition = routeDefinition;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getRouteDefinition() {
        return routeDefinition;
    }

    public void setRouteDefinition(String routeDefinition) {
        this.routeDefinition = routeDefinition;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "EISRoute{" +
                "id=" + id +
                ", routeName='" + routeName + '\'' +
                ", createDate=" + createDate +
                ", seq=" + seq +
                ", routeDefinition='" + routeDefinition + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
