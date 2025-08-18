package tz.co.itrust.vfd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tz.co.itrust.vfd.entity.VfdReceipt;

import java.util.List;
import java.util.Optional;

@Repository
public interface VfdReceiptRepository extends JpaRepository<VfdReceipt, Long> {
    
    // Existing methods
    Optional<VfdReceipt> findByCustinvoiceno(String custinvoiceno);
    
    List<VfdReceipt> findByCustidtype(Integer custidtype);
    
    List<VfdReceipt> findByPaytype(Integer paytype);
    
    List<VfdReceipt> findByDevicenumber(String devicenumber);
    
    // New VFD-specific query methods
    Optional<VfdReceipt> findByRctvcode(String rctvcode);
    
    Optional<VfdReceipt> findByZnumber(String znumber);
    
    List<VfdReceipt> findByVfdStatus(String status);
    
    List<VfdReceipt> findByVfdStatusAndVfdHttpStatus(String status, Integer httpStatus);
    
    List<VfdReceipt> findByIdateBetween(String startDate, String endDate);
    
    List<VfdReceipt> findByVfdStatusOrderByCreatedAtDesc(String status);
    
    Optional<VfdReceipt> findByRctvcodeOrZnumber(String rctvcode, String znumber);
    
    // Enhanced duplicate prevention methods
    Optional<VfdReceipt> findByCustinvoicenoAndVfdStatus(String custinvoiceno, String vfdStatus);
    
    Optional<VfdReceipt> findByCustinvoicenoAndVfdStatusAndRctvcodeIsNotNull(String custinvoiceno, String vfdStatus);
}
