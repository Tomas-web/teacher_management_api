package management.teacher_management_api.domain.services.rtc;

public interface PackableEx extends Packable {
    void unmarshal(ByteBuf in);
}
