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
