import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class AuthPage extends StatefulWidget {
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
                  onPressed:() async {
                    final String result = await platform.invokeMethod('signIn');
                    print(result);
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
