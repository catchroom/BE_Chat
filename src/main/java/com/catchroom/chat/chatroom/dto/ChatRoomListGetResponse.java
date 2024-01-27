package com.catchroom.chat.chatroom.dto;

import com.catchroom.chat.chatroom.type.ChatRoomState;
import com.catchroom.chat.chatroom.type.DealState;
import com.catchroom.chat.message.dto.ChatMessageDto;
import com.catchroom.chat.message.type.UserIdentity;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomListGetResponse implements Serializable {
    private String chatRoomNumber;

    private Long buyerId;

    private Long sellerId;

    private Long productId;

    private String accommodationName;

    private int sellPrice;

    private UserIdentity loginUserIdentity;

    private String accommodationUrl;

    private String myNickName;

    private String partnerNickName;

    private DealState dealState;

    private ChatRoomState buyerState;

    private ChatRoomState sellerState;

    private ChatMessageDto lastChatmessageDto;

    private Boolean isNego;


    public void updateChatMessageDto(ChatMessageDto chatMessageDto) {
        this.lastChatmessageDto = chatMessageDto;
    }

    public void changePartnerInfo() {
        String tmp = myNickName;
        this.myNickName = partnerNickName;
        this.partnerNickName = tmp;

        if (this.loginUserIdentity.equals(UserIdentity.SELLER)) {
            this.loginUserIdentity = UserIdentity.BUYER;
        } else if (this.loginUserIdentity.equals(UserIdentity.BUYER)) {
            this.loginUserIdentity = UserIdentity.SELLER;
        }
    }

    @Override
    public String toString() {
        return "ChatRoomListGetResponse{" +
                "chatRoomNumber='" + chatRoomNumber + '\'' +
                ", buyerId=" + buyerId +
                ", sellerId=" + sellerId +
                ", productId=" + productId +
                ", accommodationName='" + accommodationName + '\'' +
                ", sellPrice=" + sellPrice +
                ", loginUserIdentity=" + loginUserIdentity +
                ", accommodationUrl='" + accommodationUrl + '\'' +
                ", myNickName='" + myNickName + '\'' +
                ", partnerNickName='" + partnerNickName + '\'' +
                ", dealState=" + dealState +
                ", buyerState=" + buyerState +
                ", sellerState=" + sellerState +
                ", lastChatmessageDto=" + lastChatmessageDto +
                ", isNego=" + isNego +
                '}';
    }
}
