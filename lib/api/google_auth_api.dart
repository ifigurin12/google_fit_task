import 'package:google_sign_in/google_sign_in.dart';

class GoogleAuthApi {
  final _googleSignIn = GoogleSignIn();
  GoogleSignInAccount? signIn;
  List<bool> authorizedScopes = [];
  final List<String> scopes = <String>[
    'https://www.googleapis.com/auth/fitness.activity.read',
    'https://www.googleapis.com/auth/fitness.blood_glucose.read',
    'https://www.googleapis.com/auth/fitness.heart_rate.read',
  ];

  Future<bool> isLogedIn() async {
    return _googleSignIn.isSignedIn();
  }

  Future<GoogleSignInAccount?> login() async {
    signIn = await _googleSignIn.signIn();
    return signIn;
  }

  Future<bool> handleScopes() async {
    bool _isAuthorized = await _googleSignIn.requestScopes(scopes);
    return _isAuthorized;
  }

  Future<GoogleSignInAccount?> logout() async {
    _googleSignIn.disconnect();
  }
}
