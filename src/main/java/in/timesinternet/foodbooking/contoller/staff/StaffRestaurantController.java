package in.timesinternet.foodbooking.contoller.staff;

import in.timesinternet.foodbooking.dto.request.PincodeDto;
import in.timesinternet.foodbooking.dto.request.AvalibilityDto;
import in.timesinternet.foodbooking.dto.request.RestaurantUpdateDto;
import in.timesinternet.foodbooking.entity.Image;
import in.timesinternet.foodbooking.entity.Restaurant;
import in.timesinternet.foodbooking.entity.Serviceability;
import in.timesinternet.foodbooking.repository.ImageRepository;
import in.timesinternet.foodbooking.service.RestaurantService;
import in.timesinternet.foodbooking.service.impl.BindingResultService;
import in.timesinternet.foodbooking.service.PincodeService;

import in.timesinternet.foodbooking.util.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

import javax.validation.Valid;


@RestController
@RequestMapping(value = "/api/staff/restaurant")
public class  StaffRestaurantController {

    @Autowired
    ImageRepository imageRepository;
    @Autowired
    ImageService imageService;
    @Autowired
    RestaurantService restaurantService;

    @Autowired
    BindingResultService bindingResultService;

    @PatchMapping(value = "/logo")
    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_MANAGER')")
    void updateRestaurantLogo(@RequestParam MultipartFile logo, HttpServletRequest request) {

        Integer restaurantId=(Integer)request.getAttribute("restaurantId");
        String userEmail= (String)request.getAttribute("userEmail");
        restaurantService.updateRestaurantLogo(logo, restaurantId, userEmail);
    }


    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_MANAGER')")
    ResponseEntity<Restaurant> getRestaurant(HttpServletRequest request){
        Integer restaurantId=(Integer)request.getAttribute("restaurantId");
        return  ResponseEntity.ok(restaurantService.getRestaurant(restaurantId));
    }

    @PatchMapping("")
    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_MANAGER')")
    ResponseEntity<Restaurant> updateRestaurant(@RequestBody @Valid RestaurantUpdateDto restaurantUpdateDto, HttpServletRequest request, BindingResult bindingResult){
        bindingResultService.validate(bindingResult);
        Integer restaurantId=(Integer)request.getAttribute("restaurantId");
        return  ResponseEntity.ok(restaurantService.updateRestaurant(restaurantUpdateDto,restaurantId));
    }

    @Autowired
    PincodeService pincodeService;
//    @PostMapping("/pincode")
//    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_MANAGER')")
//    ResponseEntity<Serviceability> addPincode(@RequestBody PincodeDto pincodeDto, HttpServletRequest request){
//        Integer restaurantId = (Integer) request.getAttribute("restaurantId");
//
//        return ResponseEntity.ok(pincodeService.addPincode(pincodeDto, restaurantId));
//    }
    @GetMapping("/pincode")
    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_MANAGER')")
    ResponseEntity<List<Serviceability>> getPincode(HttpServletRequest request)
    {
        Integer resturantId=(Integer) request.getAttribute("restaurantId");
        return ResponseEntity.ok(pincodeService.getPincode(resturantId));

    }

    @PostMapping("/pincode")
    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_MANAGER')")
    ResponseEntity<List<Serviceability>> addPincode(@RequestBody List<PincodeDto>pincodeDto, HttpServletRequest request){
        Integer restaurantId = (Integer) request.getAttribute("restaurantId");

        return ResponseEntity.ok(pincodeService.addPincode(pincodeDto, restaurantId));
    }


    @DeleteMapping("/pincode/{pincodeId}")
    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_MANAGER')")
    ResponseEntity<Serviceability> deletePincode(@PathVariable Integer pincodeId, HttpServletRequest request){
        Integer restaurantId=(Integer) request.getAttribute("restaurantId");

        return  ResponseEntity.ok(pincodeService.deletePincode(pincodeId, restaurantId));
    }

    @PatchMapping("/pincode")
    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_MANAGER')")
    ResponseEntity<Serviceability> updatePincode(@RequestBody AvalibilityDto avalibilityDto,HttpServletRequest request)
    {
        Integer restaurantId=(Integer) request.getAttribute("restaurantId");
        return ResponseEntity.ok(pincodeService.updatePincode(avalibilityDto,restaurantId));
    }

    @PostMapping("/pincode/image")
    ResponseEntity<Image> uploadCouponImage(@RequestParam MultipartFile coupanImage)
    {
    return ResponseEntity.ok(imageRepository.save(imageService.uploadImage(coupanImage)));
    }

}
//    @PostMapping("/item/image")
////    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_MANAGER')")
//    ResponseEntity<Image> uploadItemImage(@RequestParam MultipartFile itemImage){
////        Integer restaurantId = (Integer)  request.getAttribute("restaurantId");
//        return ResponseEntity.ok(imageRepository.save(imageService.uploadImage(itemImage)));
//    }