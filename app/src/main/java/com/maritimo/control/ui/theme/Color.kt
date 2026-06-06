package com.maritimo.control.ui.theme

import androidx.compose.ui.graphics.Color

// ═══════════════════════════════════════════════════════════════
// 🎨 Control Marítimo UTE — Sistema de Diseño Premium (Blue & White Edition)
// ═══════════════════════════════════════════════════════════════

// ── Azul Principal (Brand Colors) ──────────────────────────────
val PrimaryBlue     = Color(0xFF0B2545)  // Azul Marino Oscuro - Institucional
val DarkBlue        = Color(0xFF0A1E36)  // Azul Marino Aún Más Oscuro
val LightBlue       = Color(0xFFE2E9F3)  // Azul Acero Muy Claro para Contenedores
val AccentBlue      = Color(0xFF134074)  // Azul Acero para botones y elementos activos
val SoftBlue        = Color(0xFFEDF2F8)  // Fondo Azul Sutil

// ── Superficies y Fondos ───────────────────────────────────────
val BackgroundColor = Color(0xFFF4F6F9)  // Gris claro limpio para paneles
val SurfaceColor    = Color(0xFFFFFFFF)  // Blanco puro
val SurfaceElevated = Color(0xFFFFFFFF)
val PageBg          = Color(0xFFEBF0F6)  // Gris azulado claro para fondos secundarios

// ── Textos ─────────────────────────────────────────────────────
val TextPrimary     = Color(0xFF0F172A)  // Slate 900 (Casi negro azulado)
val TextSecondary   = Color(0xFF475569)  // Slate 600 (Gris oscuro)
val TextTertiary    = Color(0xFF94A3B8)  // Slate 400 (Gris medio)
val TextOnDark      = Color(0xFFFFFFFF)

// ── Semánticos (Ajustados para armonía) ────────────────────────
val Success         = Color(0xFF10B981)  // Esmeralda
val ErrorColor      = Color(0xFFEF4444)  // Rojo suave
val Warning         = Color(0xFFF59E0B)  // Ámbar
val Info            = Color(0xFF3B82F6)  // Azul Info
val PremiumGold     = Color(0xFFFFD700)  // Dorado Premium
val PrimaryRed      = Color(0xFFE11D48)  // Rojo Intenso para acentos (Rose 600)

// ── Bordes ─────────────────────────────────────────────────────
val Border          = Color(0xFFE2E8F0)
val BorderFocus     = Color(0xFF3B82F6)
val Divider         = Color(0xFFF1F5F9)

// ── Admin Light Theme ──────────────────────────────────────────
val AdminBg         = BackgroundColor
val AdminCard       = SurfaceColor
val AdminBorder     = Border
val AdminText       = TextPrimary
val AdminMuted      = TextSecondary

// ── Aliases de compatibilidad ──────────────────────────────────
val LightRed        = LightBlue
val DarkRed         = DarkBlue
val TextFaint       = TextTertiary
val SkyBlue         = AccentBlue

val GoldPrimary     = PremiumGold
val GoldDark        = Color(0xFFB8860B)
val GoldLight       = Color(0xFFFFFACD)
val GoldSecondary   = Color(0xFFDAA520)
val DeepDark        = TextPrimary
val SurfaceDark     = SurfaceColor
val Background      = BackgroundColor
val Surface         = SurfaceColor

// ── Gradientes ─────────────────────────────────────────────────
val BlueGradient    = listOf(PrimaryBlue, AccentBlue)
val SoftBlueGradient = listOf(LightBlue, SoftBlue)
val HeroGradient    = listOf(PrimaryBlue, DarkBlue)
val GoldGradient    = listOf(PremiumGold, Color(0xFFDAA520))
val DarkGradient    = listOf(Color(0xFF0F172A), Color(0xFF1E293B))

// ── Tech Theme (Nuevo estilo inmersivo) ─────────────────────────
val DarkBackground  = Color(0xFF060B13)  // Azul Oscuro Profundo Tecnológico
val DarkSurface     = Color(0xFF0C1726)  // Tarjetas oscuras translúcidas
val ElectricCyan    = Color(0xFF00E5FF)  // Cian Neón
val TechBlue        = Color(0xFF007EA7)  // Azul Eléctrico
val NeonGlow        = Color(0xFF00A8E8)  // Cyan Eléctrico
val GlassBorder     = Color(0xFF1E2E4A)  // Bordes sutiles para Glassmorphism
val TextFaintTech   = Color(0xFF8D99AE)  // Textos secundarios apagados
val TechGradient    = listOf(DarkBackground, Color(0xFF0B1B33))
val CyanGradient    = listOf(TechBlue, NeonGlow)

// ── Paleta de Colores "Marítimo Profesional" (Personalizada) ───────
val AzulAbisal      = Color(0xFF0A1128)  // Azul Marino Profundo (Fondo)
val AzulAcero       = Color(0xFF007EA7)  // Acentor y enlaces activos
val CianElectrico   = Color(0xFF00A8E8)  // Acento secundario neón
val BlancoHielo     = Color(0xFFF4F7F6)  // Tarjetas y contenedores
val VerdeEsmeralda  = Color(0xFF2EC4B6)  // Estado Exitoso
val AmbarAlerta     = Color(0xFFFF9F1C)  // Estado Advertencia
val RojoCoral       = Color(0xFFE71D36)  // Estado Crítico
val AbisalGradient  = listOf(AzulAbisal, Color(0xFF131D3F))

