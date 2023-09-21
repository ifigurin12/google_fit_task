part of 'google_auth_bloc.dart';

@immutable
sealed class GoogleAuthEvent {
}
class GoogleSignInEvent extends GoogleAuthEvent {}
class GoogleIsSignedInEvent extends GoogleAuthEvent {}
