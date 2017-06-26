package bootapp.service;

import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * UserDetailsService を拡張したユーザー・サービス.
 */
public interface UserService extends UserDetailsService {
    // アプリのユーザー処理に必要なサービスを追加
}
