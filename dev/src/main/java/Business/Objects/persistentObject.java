package Business.Objects;

public interface persistentObject<T> {
    //boolean insert();
    //boolean update();
    //boolean delete();
    T toDTO();
}
