# 누가 그린 기린 그림
<img src="https://github.com/TAE-COLA/NGGG_Android/blob/0f031c9419bea7e1613347b91d0ca6412d813f73/NGGG_Thumbnail.png" width=500/>
2024 비즈앱 개발 파트 겨울 인턴 프로젝트

## 프로젝트 설명

**누가 그린 기린 그림**은 AI가 그리는데 사용된 프롬프트를
사용자가 맞추는 퀴즈 앱입니다.

## 프로젝트 기획
[Figma](https://www.figma.com/file/1X4Dw5CyLXMR67GfqNrJO3/%EB%88%84%EA%B0%80-%EA%B7%B8%EB%A6%B0-%EA%B8%B0%EB%A6%B0-%EA%B7%B8%EB%A6%BC%3F?type=design&node-id=1313%3A1900&mode=design&t=Twa7FlzcicBlBACg-1)

## 아키텍처 구성
- Clean Architecture
- Multi Module
- MVI

## 사용 기술
**View**
- Jetpack Compose
- View-Base

**Async**
- Coroutine
- Flow

**Network**
- Retrofit
  - Karlo API
- Firebase SDK
  - Firestore
  - Firebase Storage
- KAKAO SDK
  - Login
  - Share

**DI**
- Hilt

**Image**
- Coil

**Serialization**
- Kotlin-Serialization

**Test**
- Junit4
- Mockk
- Assertj

## 사용 방법
**Kakao SDK**
1. Kakao hask key 생성 후 등록
- [Kakao Developers](https://developers.kakao.com/docs/latest/ko/android/getting-started#before-you-begin-add-key-hash)
- 위 링크 참고하여 해시 키 생성 후 애플리케이션에 등록해야 합니다.

**Firebase**
1. 프로젝트에 google.services.json 파일 추가
- app/google-services.json 경로에 추가
