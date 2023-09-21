import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:google_fit_test_task/bloc/bloc_auth/google_auth_bloc.dart';

class AuthPage extends StatefulWidget {
  static const routeName = '/authPage';
  State<AuthPage> createState() => _AuthPageState();
}

class _AuthPageState extends State<AuthPage> {
  static const platform = MethodChannel('auth_google');
  @override
  Widget build(BuildContext context) {
    final _size = MediaQuery.of(context).size;
    return Scaffold(
      body: SafeArea(
        child: Padding(
          padding: const EdgeInsets.all(16.0),
          child: Center(
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                const Text(
                  'Добро пожаловать в приложение google fit TT',
                  style: TextStyle(fontSize: 25, fontWeight: FontWeight.w600),
                  textAlign: TextAlign.center,
                ),
                SizedBox(
                  height: _size.height * 0.1,
                ),
                const Text(
                  'Для функционирования приложения требуется вход',
                  style: TextStyle(fontSize: 17, fontWeight: FontWeight.w500),
                  textAlign: TextAlign.center,
                ),
                SizedBox(
                  height: _size.height * 0.05,
                ),
                FilledButton(
                  onPressed: () {
                    BlocProvider.of<GoogleAuthBloc>(context).add(
                      GoogleSignInEvent(),
                    );
                  },
                  child: Text('Войти с помощью google'),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
