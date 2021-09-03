package in.timesinternet.foodbooking.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import in.timesinternet.foodbooking.entity.enumeration.PackageStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Package implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @JsonFormat( pattern = "dd-MM-yyyy hh:mm:ss")
    @CreationTimestamp
    Date createdAt;

    @JsonFormat( pattern = "dd-MM-yyyy hh:mm:ss")
    @UpdateTimestamp
    Date updatedAt;

    @Enumerated(EnumType.STRING)
    PackageStatus status;

    //relationship


    @OneToOne(mappedBy = "pack")
    @JsonIgnore
    Order order;

    @OneToOne
    PackageDelivery currentPackageDelivery;

    @OneToMany(mappedBy = "pack", cascade = CascadeType.ALL)
    List<PackageDelivery> packageDeliveryList= new ArrayList<>();
    public void addPackageDelivery(PackageDelivery packageDelivery){
        packageDelivery.setPack(this);
        packageDeliveryList.add(packageDelivery);
    }

    @Transient
    List<String> next= new ArrayList<>();

}
