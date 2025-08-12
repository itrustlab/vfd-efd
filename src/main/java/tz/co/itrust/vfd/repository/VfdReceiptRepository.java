package tz.co.itrust.vfd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tz.co.itrust.vfd.entity.VfdReceipt;

import java.util.List;
import java.util.Optional;

@Repository
public interface VfdReceiptRepository extends JpaRepository<VfdReceipt, Long> {
    
    Optional<VfdReceipt> findByCustinvoiceno(String custinvoiceno);
    
    List<VfdReceipt> findByCustidtype(Integer custidtype);
    
    List<VfdReceipt> findByPaytype(Integer paytype);
    
    List<VfdReceipt> findByDevicenumber(String devicenumber);
}
