package management.teacher_management_api.usercases.calls;

import lombok.RequiredArgsConstructor;
import lombok.val;
import management.teacher_management_api.domain.services.rtc.RtcTokenBuilder;
import management.teacher_management_api.drivers.api.payloads.calls.CreateCallRoomResponse;
import management.teacher_management_api.ports.persistence.CallRoomsDao;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateCallRoomUsecase {
    private static final String appId = "55e40a7b1cae4227817ade1de2c8ebf4";
    private static final String certificate = "a14719831bdd4e1f806a1d656838289c";
    private static final int expirationTimeInSeconds = 24 * 60 * 60;

    private final CallRoomsDao callRoomsDao;

    public CreateCallRoomResponse execute(long userId, long targetId) {
        val channelName = UUID.randomUUID().toString();
        int timestamp = (int)(System.currentTimeMillis() / 1000 + expirationTimeInSeconds);

        callRoomsDao.delete(userId, targetId);

        val rtcToken = new RtcTokenBuilder();
        val token = rtcToken.buildTokenWithUid(appId, certificate, channelName, 0, RtcTokenBuilder.Role.Role_Publisher, timestamp);

        callRoomsDao.create(userId, targetId, channelName, token);

        return CreateCallRoomResponse.builder().token(token).channelName(channelName).build();
    }
}
