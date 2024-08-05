package com.ssafy.withme.repository.chat;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.withme.domain.chat.ChatRoom;
import com.ssafy.withme.domain.chat.QChatRoom;
import com.ssafy.withme.domain.user.QUser;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.ssafy.withme.domain.chat.QChatRoom.chatRoom;
import static com.ssafy.withme.domain.user.QUser.user;

@RequiredArgsConstructor
public class ChatRoomRepositoryImpl implements ChatRoomRepositoryCustom{

    private final JPAQueryFactory qf;

    @Override
    public List<ChatRoom> findByUserId(Long userId) {

        return qf.selectFrom(chatRoom)
                .where(chatRoom.user.id.eq(userId).or(chatRoom.partner.id.eq(userId)))
                .fetch();
    }

    @Override
    public Optional<ChatRoom> findByUserIdAndPartnerId(Long userId, Long partnerId) {

        return Optional.ofNullable(qf.selectFrom(chatRoom)
                .where(
                        (chatRoom.user.id.eq(userId).and(chatRoom.partner.id.eq(partnerId)))
                                .or(chatRoom.user.id.eq(partnerId).and(chatRoom.partner.id.eq(userId)))
                )
                .fetchOne());
    }

    @Override
    public ChatRoom findByUserIdAndRoomId(Long userId, Long roomId) {

        QChatRoom chatRoom = QChatRoom.chatRoom;
        QUser user = QUser.user;

        return qf.selectFrom(chatRoom)
                .join(chatRoom.user, user).fetchJoin()
                .join(chatRoom.partner, user).fetchJoin()
                .where(chatRoom.user.id.eq(userId), chatRoom.id.eq(roomId))
                .fetchOne();
    }
}
