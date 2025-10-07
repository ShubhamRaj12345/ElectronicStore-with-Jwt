package com.electronic.Service.Impl;

import com.electronic.Dto.PageableResponse;
import com.electronic.Dto.UserDto;
import com.electronic.Entity.Role;
import com.electronic.Entity.User;
import com.electronic.HandlingException.ResourceNotFoundException;
import com.electronic.Helper.HelperClass;
import com.electronic.Repository.RoleRepository;
import com.electronic.Repository.UserRepository;
import com.electronic.Service.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Value("${user.profile.image.path}")
    private String imagePath;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserDto createUser(UserDto userDto) {

        String string = UUID.randomUUID().toString();
        userDto.setUserId(string);
        // dto to entit
         User saveUser=  dtoToEntity(userDto);

        String encode = passwordEncoder.encode(userDto.getPassword());
        saveUser.setPassword(encode);
        Role role = new Role();
        role.setRoleID(UUID.randomUUID().toString());
        role.setName("ROLE_NORMAL");
        Role roleNormal = roleRepository.findByName("ROLE_NORMAL").orElse(role);
          saveUser.setRoles(Set.of(roleNormal));
        User save = userRepository.save(saveUser);
        // for set the userId

        // entity  to dto
        UserDto userDto1 = entityToDto(save);
        return userDto1;
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found With given Id"));
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setGender(userDto.getGender());
        user.setAbout(userDto.getAbout());
        user.setImageName(userDto.getImageName());
        User save = userRepository.save(user);
        UserDto userDto1 = entityToDto(save);
        return userDto1;
    }

    @Override
    public void deleteUser(String userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Not found with given Id "));

        String fullpath = imagePath + user.getImageName();

       try{
           Path path = Paths.get(fullpath);
           Files.delete(path);
       }
       catch (NoSuchFileException x)
       {
           logger.info("user image not found");

           x.printStackTrace();
       }
       catch (Exception e)
       {
          e.printStackTrace();
       }
        userRepository.delete(user);
    }

    @Override
    public PageableResponse<UserDto> findAllUser(int pageNumber, int pageSize , String sortBy, String sortDir) {

     Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
//        Sort sort = Sort.by(sortBy);
        Pageable pageable = PageRequest.of(pageNumber, pageSize , sort);
        Page<User> all = userRepository.findAll(pageable);


//        List<User> content = all.getContent();
//        // we need to return UserDto
//        // yaha ek ek user milega usko collect karenge
//        List<UserDto> collect = content.stream().map(user -> entityToDto(user)).collect(Collectors.toList());
//
//
//        PageableResponse<UserDto> response= new PageableResponse<>();
//        response.setContents(collect);
//        response.setPageNumber(all.getNumber());
//        response.setTotalPage(all.getTotalPages());
//        response.setPageSize(all.getSize());
//        response.setTotalElements(all.getTotalElements());
//        response.setLastPage(all.isLast());
       // return response;

        PageableResponse<UserDto> pageableresponse = HelperClass.getPageableresponse(all, UserDto.class);
        return pageableresponse;


    }

    @Override
    public UserDto getSingleUserById(String userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with given id"));
        UserDto userDto = entityToDto(user);
        return userDto;
    }

    @Override
    public UserDto getSingleUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found with given email"));
        UserDto userDto = entityToDto(user);
        return userDto;
    }

    @Override
    public List<UserDto> searchUser(String keyword) {
        List<User> byNameContaining = userRepository.findByNameContaining(keyword);

        List<UserDto> collect = byNameContaining.stream().map(user -> entityToDto(user)).collect(Collectors.toList());

        return collect;
    }


    private UserDto entityToDto(User saveUser) {
//        UserDto build = UserDto.builder()
//                .userId(saveUser.getUserId())
//                .about(saveUser.getAbout())
//                .email(saveUser.getEmail())
//                .name(saveUser.getName())
//                .gender(saveUser.getGender())
//                .imageName(saveUser.getImageName())
//                .password(saveUser.getPassword()).build();

      //  return  build;


        UserDto map = modelMapper.map(saveUser, UserDto.class);
        return map;

    }

    private User dtoToEntity(UserDto userDto) {

//        User us =  new User();
//        us.setUserId(userDto.getUserId());
//        us.setName(userDto.getName());
//        us.setEmail(userDto.getEmail());
//        us.setGender(userDto.getGender());
//        us.setPassword(userDto.getPassword());
//        us.setImageName(userDto.getImageName());
//        us.setAbout(userDto.getAbout());

        //return  us;

      return   modelMapper.map(userDto,User.class);
    }


}
