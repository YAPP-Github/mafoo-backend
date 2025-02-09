package kr.mafoo.user.domain.key;

import kr.mafoo.user.enums.IdentityProvider;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@RequiredArgsConstructor
@Data
public class SocialMemberEntityKey implements Serializable {
    private final IdentityProvider identityProvider;
    private final String id;
}
